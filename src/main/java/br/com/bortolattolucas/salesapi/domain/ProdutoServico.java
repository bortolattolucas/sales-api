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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "produto_servico")
public class ProdutoServico extends BaseDomain {

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private Double preco;

    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoRecurso tipo;

    @NotNull
    @Builder.Default
    private Boolean ativo = true;
}
