package br.com.bortolattolucas.salesapi.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class Pedido extends BaseDomain {

    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemPedido> itens = new HashSet<>();

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_total")
    private Double valorTotal = 0.0;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_total_servicos")
    private Double valorTotalServicos = 0.0;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_total_produtos")
    private Double valorTotalProdutos = 0.0;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "valor_original_produtos")
    private Double valorOriginalProdutos = 0.0;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "prc_desconto_produtos")
    private Double prcDescontoProdutos = 0.0;

    @NotNull
    @Builder.Default
    private Boolean aberto = true;

    public UUID[] getItensIds() {
        return this.getItens()
                .stream()
                .map(itemPedido ->
                        itemPedido.getProdutoServico().getId()
                ).toArray(UUID[]::new);
    }

    public void mapearItens() {
        this.getItens().forEach(itemPedido -> itemPedido.setPedido(this));
    }
}
