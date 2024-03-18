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

    public static PedidoDto toDto(Pedido pedido) {
        return PedidoDto.builder()
                .id(pedido.getId().toString())
                .valorTotal(pedido.getValorTotal())
                .valorTotalServicos(pedido.getValorTotalServicos())
                .valorTotalProdutos(pedido.getValorTotalProdutos())
                .valorOriginalProdutos(pedido.getValorOriginalProdutos())
                .prcDescontoProdutos(pedido.getPrcDescontoProdutos())
                .itens(pedido.getItens().stream()
                        .map(PedidoMapper::toDto)
                        .collect(Collectors.toSet()))
                .aberto(pedido.getAberto())
                .createdAt(pedido.getCreatedAt())
                .build();
    }

    public static ItemPedidoDto toDto(ItemPedido itemPedido) {
        return ItemPedidoDto.builder()
                .produtoServicoId(itemPedido.getProdutoServico().getId())
                .produtoServicoNome(itemPedido.getProdutoServico().getNome())
                .produtoServicoDescricao(itemPedido.getProdutoServico().getDescricao())
                .produtoServicoTipo(itemPedido.getProdutoServico().getTipo().getDescricao())
                .quantidade(itemPedido.getQuantidade())
                .valorUnitario(itemPedido.getValorUnitario())
                .valorTotal(itemPedido.getValorTotal())
                .build();
    }
}
