package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProdutoServicoService extends BaseService<ProdutoServico, UUID> {
    Page<ProdutoServico> findPage(int page, int size, String orderBy, String direction, String nome, Double preco,
                                  Double precoMinimo, Double precoMaximo, String descricao, String tipo, Boolean ativo);

    void update(UUID id, ProdutoServico entity);
}
