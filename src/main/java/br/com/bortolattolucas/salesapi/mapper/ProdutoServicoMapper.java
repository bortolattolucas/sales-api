package br.com.bortolattolucas.salesapi.mapper;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.dto.ProdutoServicoDto;
import br.com.bortolattolucas.salesapi.enums.TipoRecurso;

public class ProdutoServicoMapper {

    public static ProdutoServico toEntity(ProdutoServicoDto produtoServicoDto) {
        return ProdutoServico.builder()
                .nome(produtoServicoDto.getNome())
                .preco(produtoServicoDto.getPreco())
                .descricao(produtoServicoDto.getDescricao())
                .tipo(TipoRecurso.fromDescricao(produtoServicoDto.getTipo()))
                .ativo(produtoServicoDto.isAtivo())
                .build();
    }

    public static ProdutoServicoDto toDto(ProdutoServico produtoServico) {
        return ProdutoServicoDto.builder()
                .id(produtoServico.getId().toString())
                .nome(produtoServico.getNome())
                .preco(produtoServico.getPreco())
                .descricao(produtoServico.getDescricao())
                .tipo(produtoServico.getTipo().getDescricao())
                .ativo(produtoServico.isAtivo())
                .build();
    }
}
