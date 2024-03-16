package br.com.bortolattolucas.salesapi.mapper;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.dto.PedidoDto;

import java.util.stream.Collectors;

public class PedidoMapper {

    public static Pedido toEntity(PedidoDto pedidoDto) {
        if (pedidoDto == null) {
            return Pedido.builder().build();
        }

        return Pedido.builder()
                .itens(pedidoDto.getItens().stream()
                        .map(itemPedidoDto ->
                                ItemPedido.builder()
                                        .produtoServico(ProdutoServico.builder()
                                                .id(itemPedidoDto.getProdutoServicoId()).build())
                                        .quantidade(itemPedidoDto.getQuantidade())
                                        .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
