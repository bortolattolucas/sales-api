package br.com.bortolattolucas.salesapi;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.dto.ProdutoServicoDto;
import br.com.bortolattolucas.salesapi.enums.TipoRecurso;
import br.com.bortolattolucas.salesapi.mapper.ProdutoServicoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class TestObjectsUtil {

    public static final String BASE_HOST = "http://localhost";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProdutoServico getServicoInativo20Reais() {
        return ProdutoServico.builder()
                .id(UUID.fromString("f01909be-9acb-4677-9777-728428717690"))
                .nome("Serviço 1")
                .preco(20.0)
                .descricao("Descrição do serviço 1")
                .tipo(TipoRecurso.SERVICO)
                .ativo(false)
                .build();
    }

    public static ProdutoServico getProdutoAtivo10Reais() {
        return ProdutoServico.builder()
                .id(UUID.fromString("4b6f4923-5cbe-4264-aa25-8274d6ad7f3a"))
                .nome("Produto 1")
                .preco(10.0)
                .descricao("Descrição do produto 1")
                .tipo(TipoRecurso.PRODUTO)
                .ativo(true)
                .build();
    }

    public static ProdutoServico getProdutoServicoMock() {
        return ProdutoServico.builder()
                .id(UUID.fromString("6d3259c4-ea3f-4828-a48b-b8aa39afd22f"))
                .nome("Produto")
                .preco(100.50)
                .descricao("Descrição do Produto")
                .tipo(TipoRecurso.PRODUTO)
                .ativo(true)
                .build();
    }

    public static String getProdutoServicoJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(ProdutoServicoMapper.toDto(getProdutoServicoMock()));
    }

    public static String getProdutoServicoJsonVazio() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ProdutoServicoDto.builder()
                        .build());
    }

    public static String getProdutoServicoJsonInvalido() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ProdutoServicoDto.builder()
                        .nome(" ")
                        .preco(-1.0)
                        .tipo("INVALIDO")
                        .build());
    }
}
