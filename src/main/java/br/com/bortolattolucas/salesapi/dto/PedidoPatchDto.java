package br.com.bortolattolucas.salesapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoPatchDto {

    @NotNull(message = "Não pode ser vazio")
    @PositiveOrZero(message = "Deve ser um valor positivo")
    private Double prcDescontoProdutos;

}
