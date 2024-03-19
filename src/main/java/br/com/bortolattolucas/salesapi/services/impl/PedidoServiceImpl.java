package br.com.bortolattolucas.salesapi.services.impl;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.domain.QPedido;
import br.com.bortolattolucas.salesapi.domain.QProdutoServico;
import br.com.bortolattolucas.salesapi.repositories.PedidoRepository;
import br.com.bortolattolucas.salesapi.repositories.ProdutoServicoRepository;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import br.com.bortolattolucas.salesapi.services.exceptions.DataIntegrityException;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        if (!pedido.getItens().isEmpty()) {
            configurarPedido(pedido);
        }

        return getRepository().save(pedido);
    }

    private void configurarPedido(Pedido pedido) {
        pedido.mapearItens();

        Iterable<ProdutoServico> produtoServicos = buscarItensAdicionados(pedido);

        validarAusenciaItensInativos(produtoServicos);

        configurarValoresItens(pedido, produtoServicos);
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
            throw new DataIntegrityException("Um pedido não pode ter itens inativos.", idsInativos);
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
    public Page<Pedido> findPage(int page, int size, String orderBy, String direction, Double valorTotal,
                                 Double valorTotalMinimo, Double valorTotalMaximo, Boolean aberto,
                                 LocalDateTime createdAt, LocalDateTime createdAtMin, LocalDateTime createdAtMax) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (valorTotal != null) {
            booleanBuilder.and(QPedido.pedido.valorTotal.eq(valorTotal));
        }

        if (valorTotalMinimo != null) {
            booleanBuilder.and(QPedido.pedido.valorTotal.goe(valorTotalMinimo));
        }

        if (valorTotalMaximo != null) {
            booleanBuilder.and(QPedido.pedido.valorTotal.loe(valorTotalMaximo));
        }

        if (aberto != null) {
            booleanBuilder.and(QPedido.pedido.aberto.eq(aberto));
        }

        if (createdAt != null) {
            booleanBuilder.and(QPedido.pedido.createdAt.eq(createdAt));
        }

        if (createdAtMin != null) {
            booleanBuilder.and(QPedido.pedido.createdAt.goe(createdAtMin));
        }

        if (createdAtMax != null) {
            booleanBuilder.and(QPedido.pedido.createdAt.loe(createdAtMax));
        }

        return pedidoRepository.findAll(booleanBuilder, PageRequest.of(page, size,
                Sort.Direction.valueOf(direction), orderBy));
    }

    @Override
    public void fecharPedido(UUID id) {
        Pedido pedido = findById(id);

        if (!pedido.getAberto()) {
            throw new DataIntegrityException("Não é possível fechar pedidos que não estejam abertos", Map.of(
                    pedido.getId().toString(), "Pedido fechado"
            ));
        }

        pedido.setAberto(false);
        getRepository().save(pedido);
    }

    @Override
    @Transactional
    public void adicionarItem(UUID id, ItemPedido item) {
        Pedido pedido = findById(id);

        if (!pedido.getAberto()) {
            throw new DataIntegrityException("Não é possível adicionar itens a pedidos que não estejam abertos", Map.of(
                    pedido.getId().toString(), "Pedido fechado"
            ));
        }

        if (pedido.possuiProdutoServico(item.getProdutoServico().getId())) {
            throw new DataIntegrityException("Não é possível adicionar o mesmo item mais de uma vez",
                    Map.of(item.getProdutoServico().getId().toString(), "Item já adicionado ao pedido"));
        }

        pedido.getItens().add(item);

        try {
            configurarPedido(pedido);
        } catch (DataIntegrityException e) {
            pedido.getItens().remove(item);
            throw e;
        }

        getRepository().save(pedido);
    }

    @Override
    public void alterarDescontoProdutos(UUID id, Double prcDescontoProdutos) {
        Pedido pedido = findById(id);

        if (!pedido.getAberto()) {
            throw new DataIntegrityException("Não é possível alterar o desconto de produtos em pedidos fechados",
                    Map.of(pedido.getId().toString(), "Pedido fechado"));
        }

        pedido.setPrcDescontoProdutos(prcDescontoProdutos);
        configurarPedido(pedido);
        getRepository().save(pedido);
    }

    @Transactional
    @Override
    public void deleteItem(UUID id, UUID idProdutoServico) {
        Pedido pedido = findById(id);

        if (!pedido.getAberto()) {
            throw new DataIntegrityException("Não é possível remover itens de pedidos fechados",
                    Map.of(pedido.getId().toString(), "Pedido fechado"));
        }

        if (!pedido.getItens().removeIf(item -> item.getProdutoServico().getId().equals(idProdutoServico))) {
            throw new DataIntegrityException("Não é possível remover itens inexistentes de um pedido",
                    Map.of(idProdutoServico.toString(), "Produto/Serviço não encontrado no pedido"));
        }

        configurarPedido(pedido);
        getRepository().save(pedido);
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
