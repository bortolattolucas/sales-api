package br.com.bortolattolucas.salesapi.repositories;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID>, QuerydslPredicateExecutor<ItemPedido> {
}
