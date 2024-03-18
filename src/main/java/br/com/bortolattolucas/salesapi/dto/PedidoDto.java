package br.com.bortolattolucas.salesapi.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    private String id;

    private Double valorTotal;

    private Double valorTotalServicos;

    private Double valorTotalProdutos;

    private Double valorOriginalProdutos;

    @PositiveOrZero(message = "Deve ser um valor positivo")
    private Double prcDescontoProdutos;

    private Boolean aberto;

    private LocalDateTime createdAt;

    @Builder.Default
    private Set<ItemPedidoDto> itens = new HashSet<>();
}
