package br.com.bortolattolucas.salesapi.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    @PositiveOrZero(message = "Deve ser um valor positivo")
    private Double prcDescontoProdutos;

    @Builder.Default
    private Set<ItemPedidoDto> itens = new HashSet<>();
}
