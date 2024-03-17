package br.com.bortolattolucas.salesapi.services.impl;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.domain.QProdutoServico;
import br.com.bortolattolucas.salesapi.repositories.PedidoRepository;
import br.com.bortolattolucas.salesapi.repositories.ProdutoServicoRepository;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import br.com.bortolattolucas.salesapi.services.exceptions.DataIntegrityException;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoServicoRepository produtoServicoRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ProdutoServicoRepository produtoServicoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoServicoRepository = produtoServicoRepository;
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        pedido.mapearItens();

        if (!pedido.getItens().isEmpty()) {
            configurarPedido(pedido);
        }

        return getRepository().save(pedido);
    }

    private void configurarPedido(Pedido pedido) {
        Iterable<ProdutoServico> itens = buscarItensAdicionados(pedido);

        validarAusenciaItensInativos(itens);

        configurarValoresItens(pedido, itens);
        configurarValoresPedido(pedido);
    }

    private Iterable<ProdutoServico> buscarItensAdicionados(Pedido pedido) {
        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(QProdutoServico.produtoServico.id.in(pedido.getItensIds()));

        return produtoServicoRepository.findAll(booleanBuilder);
    }

    private static void validarAusenciaItensInativos(Iterable<ProdutoServico> itens) {
        Set<ProdutoServico> itensInativos = StreamSupport.stream(itens.spliterator(), false)
                .filter(item -> !item.getAtivo())
                .collect(Collectors.toSet());

        if (!itensInativos.isEmpty()) {
            Map<String, String> idsInativos = new HashMap<>();
            itensInativos.forEach(item -> idsInativos.put(item.getId().toString(), "Produto/Serviço inativo"));
            throw new DataIntegrityException("Não é possível criar pedidos itens inativos.", idsInativos);
        }
    }

    private void configurarValoresItens(Pedido pedido, Iterable<ProdutoServico> itens) {
        pedido.getItens().forEach(item -> {
            Optional<ProdutoServico> dadosItem = StreamSupport.stream(itens.spliterator(), false)
                    .filter(i -> i.getId().equals(item.getProdutoServico().getId()))
                    .findFirst();

            if (dadosItem.isPresent()) {
                item.setValorUnitario(dadosItem.get().getPreco());
                item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
                item.setProdutoServico(dadosItem.get());
            }
        });
    }

    private void configurarValoresPedido(Pedido pedido) {
        pedido.setValorTotalServicos(pedido.getItens().stream()
                .filter(ItemPedido::isServico)
                .mapToDouble(ItemPedido::getValorTotal)
                .sum());

        pedido.setValorOriginalProdutos(pedido.getItens().stream()
                .filter(ItemPedido::isProduto)
                .mapToDouble(ItemPedido::getValorTotal)
                .sum());

        pedido.setValorTotalProdutos(pedido.getPrcDescontoProdutos() > 0
                ? pedido.getValorOriginalProdutos() * (1 - pedido.getPrcDescontoProdutos() / 100)
                : pedido.getValorOriginalProdutos());

        pedido.setValorTotal(pedido.getValorTotalProdutos() + pedido.getValorTotalServicos());
    }

    @Override
    public void delete(UUID id) {
        throwExceptionIfNotFound(id);
        deleteById(id);
    }

    @Override
    public JpaRepository<Pedido, UUID> getRepository() {
        return pedidoRepository;
    }
}
