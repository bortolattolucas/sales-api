package br.com.bortolattolucas.salesapi.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProdutoServicoPatchDto {

    @Pattern(regexp = "^(?=.*\\S).*$", message = "Não pode ser vazio")
    private String nome;

    @Positive(message = "Deve ser um valor positivo")
    private Double preco;

    private String descricao;

    @Pattern(regexp = "PRODUTO|SERVICO", message = "Deve ser PRODUTO ou SERVICO")
    private String tipo;

    private Boolean ativo;
}
