package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.Pedido;

import java.util.UUID;

public interface PedidoService extends BaseService<Pedido, UUID> {
    void delete(UUID id);
}
