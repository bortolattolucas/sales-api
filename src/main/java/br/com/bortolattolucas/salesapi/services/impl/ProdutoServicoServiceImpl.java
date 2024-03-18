package br.com.bortolattolucas.salesapi.services.impl;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.domain.QProdutoServico;
import br.com.bortolattolucas.salesapi.repositories.ItemPedidoRepository;
import br.com.bortolattolucas.salesapi.repositories.ProdutoServicoRepository;
import br.com.bortolattolucas.salesapi.services.ProdutoServicoService;
import br.com.bortolattolucas.salesapi.services.exceptions.DataIntegrityException;
import br.com.bortolattolucas.salesapi.utils.PatcherUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import java.util.Map;
import java.util.UUID;

import static br.com.bortolattolucas.salesapi.utils.ValueUtils.isNull;
import static br.com.bortolattolucas.salesapi.utils.ValueUtils.isNullOrEmpty;

@Slf4j
@Service
public class ProdutoServicoServiceImpl implements ProdutoServicoService {

    private final ProdutoServicoRepository produtoServicoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public ProdutoServicoServiceImpl(ProdutoServicoRepository produtoServicoRepository,
                                     ItemPedidoRepository itemPedidoRepository) {
        this.produtoServicoRepository = produtoServicoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public Page<ProdutoServico> findPage(int page, int size, String orderBy, String direction, String nome,
                                         Double preco, Double precoMinimo, Double precoMaximo, String descricao,
                                         String tipo, Boolean ativo) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!isNullOrEmpty(nome)) {
            booleanBuilder.and(QProdutoServico.produtoServico.nome.containsIgnoreCase(nome));
        }

        if (!isNull(preco)) {
            booleanBuilder.and(QProdutoServico.produtoServico.preco.eq(preco));
        }

        if (!isNull(precoMinimo)) {
            booleanBuilder.and(QProdutoServico.produtoServico.preco.goe(precoMinimo));
        }

        if (!isNull(precoMaximo)) {
            booleanBuilder.and(QProdutoServico.produtoServico.preco.loe(precoMaximo));
        }

        if (!isNullOrEmpty(descricao)) {
            booleanBuilder.and(QProdutoServico.produtoServico.descricao.containsIgnoreCase(descricao));
        }

        if (!isNullOrEmpty(tipo)) {
            booleanBuilder.and(QProdutoServico.produtoServico.tipo.stringValue().equalsIgnoreCase(tipo));
        }

        if (!isNull(ativo)) {
            booleanBuilder.and(QProdutoServico.produtoServico.ativo.eq(ativo));
        }

        return produtoServicoRepository.findAll(booleanBuilder, PageRequest.of(page, size,
                Sort.Direction.valueOf(direction), orderBy));
    }

    @Override
    public void update(UUID id, ProdutoServico entity) {
        ProdutoServico persisted = this.findById(id);

        entity.setId(id);
        entity.setCreatedAt(persisted.getCreatedAt());

        produtoServicoRepository.save(entity);
    }

    @Override
    public void updatePartial(UUID id, ProdutoServico entity) {
        ProdutoServico persisted = this.findById(id);

        try {
            PatcherUtils.patch(persisted, entity);
        } catch (IllegalAccessException e) {
            log.error("Error on patching ProdutoServico", e);
        }

        produtoServicoRepository.save(persisted);
    }

    @Override
    public void delete(UUID id) {
        throwExceptionIfNotFound(id);
        throwExceptionIfHasItemPedido(id);
        deleteById(id);
    }

    private void throwExceptionIfHasItemPedido(UUID id) {
        BooleanBuilder booleanBuilder = new BooleanBuilder()
                .and(QProdutoServico.produtoServico.id.eq(id));

        if (itemPedidoRepository.exists(booleanBuilder)) {
            throw new DataIntegrityException("Não é permitido excluir produtos associados a pedidos", Map.of(
                    id.toString(), "Produto está associado a um pedido existente"
            ));
        }
    }

    @Override
    public JpaRepository<ProdutoServico, UUID> getRepository() {
        return produtoServicoRepository;
    }
}
