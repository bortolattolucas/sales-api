package br.com.bortolattolucas.salesapi.repositories;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutoServicoRepository extends JpaRepository<ProdutoServico, UUID>,
        QuerydslPredicateExecutor<ProdutoServico> {
}
