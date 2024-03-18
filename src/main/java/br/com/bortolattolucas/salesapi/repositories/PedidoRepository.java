package br.com.bortolattolucas.salesapi.repositories;

import br.com.bortolattolucas.salesapi.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID>, QuerydslPredicateExecutor<Pedido> {
}
