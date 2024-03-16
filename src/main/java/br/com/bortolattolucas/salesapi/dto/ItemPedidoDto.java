package br.com.bortolattolucas.salesapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ItemPedidoDto {

    @NotNull(message = "Não pode ser nulo")
    private UUID produtoServicoId;

    @NotNull(message = "Não pode ser nulo")
    private Double quantidade;
}
