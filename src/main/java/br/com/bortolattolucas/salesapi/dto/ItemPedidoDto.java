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

    private String produtoServicoNome;

    private String produtoServicoDescricao;

    private String produtoServicoTipo;

    @NotNull(message = "Não pode ser nulo")
    private Double quantidade;

    private Double valorUnitario;

    private Double valorTotal;
}
