package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PedidoService extends BaseService<Pedido, UUID> {
    void delete(UUID id);

    Page<Pedido> findPage(int page, int size, String orderBy, String direction, Double valorTotal,
                          Double valorTotalMinimo, Double valorTotalMaximo, Boolean aberto, LocalDateTime createdAt,
                          LocalDateTime createdAtMin, LocalDateTime createdAtMax);

    void fecharPedido(UUID id);

    void adicionarItem(UUID id, ItemPedido entity);
}
