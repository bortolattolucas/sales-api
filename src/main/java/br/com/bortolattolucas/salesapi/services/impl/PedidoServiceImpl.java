package br.com.bortolattolucas.salesapi.services.impl;

import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.repositories.PedidoRepository;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        pedido.getItens().forEach(itemPedido -> itemPedido.setPedido(pedido));
        return getRepository().save(pedido);
    }

    @Override
    public void delete(UUID id) {
        throwExceptionIfNotFound(id);
        deleteById(id);
    }

    @Override
    public JpaRepository<Pedido, UUID> getRepository() {
        return pedidoRepository;
    }
}
