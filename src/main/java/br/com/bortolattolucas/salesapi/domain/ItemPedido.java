package br.com.bortolattolucas.salesapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static br.com.bortolattolucas.salesapi.enums.TipoRecurso.PRODUTO;
import static br.com.bortolattolucas.salesapi.enums.TipoRecurso.SERVICO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "item_pedido")
public class ItemPedido extends BaseDomain {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_servico_id")
    private ProdutoServico produtoServico;

    @NotNull
    @Positive
    private Double quantidade;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_unitario")
    private Double valorUnitario = 0.0;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_total")
    private Double valorTotal = 0.0;

    public boolean isProduto() {
        return this.produtoServico.getTipo().equals(PRODUTO);
    }

    public boolean isServico() {
        return this.produtoServico.getTipo().equals(SERVICO);
    }
}
