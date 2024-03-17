package br.com.bortolattolucas.salesapi.mapper;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.dto.ItemPedidoDto;
import br.com.bortolattolucas.salesapi.dto.PedidoDto;

import java.util.stream.Collectors;

public class PedidoMapper {

    public static Pedido toEntity(PedidoDto pedidoDto) {
        if (pedidoDto == null) {
            return Pedido.builder().build();
        }

        return Pedido.builder()
                .prcDescontoProdutos(pedidoDto.getPrcDescontoProdutos() != null
                        ? pedidoDto.getPrcDescontoProdutos()
                        : 0)
                .itens(pedidoDto.getItens().stream()
                        .map(PedidoMapper::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static ItemPedido toEntity(ItemPedidoDto itemPedidoDto) {
        return ItemPedido.builder()
                .produtoServico(ProdutoServico.builder()
                        .id(itemPedidoDto.getProdutoServicoId()).build())
                .quantidade(itemPedidoDto.getQuantidade())
                .build();
    }
}
