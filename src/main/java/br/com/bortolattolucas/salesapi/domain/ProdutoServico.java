package br.com.bortolattolucas.salesapi.domain;

import br.com.bortolattolucas.salesapi.enums.TipoRecurso;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "produto_servico")
public class ProdutoServico extends BaseDomain {

    @NotBlank
    private String nome;

    @Positive
    private double preco;

    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoRecurso tipo;

    @NotNull
    @Builder.Default
    private boolean ativo = true;
}
