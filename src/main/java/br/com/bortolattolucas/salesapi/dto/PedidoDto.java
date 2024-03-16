package br.com.bortolattolucas.salesapi.dto;

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

    @Builder.Default
    private Set<ItemPedidoDto> itens = new HashSet<>();
}
