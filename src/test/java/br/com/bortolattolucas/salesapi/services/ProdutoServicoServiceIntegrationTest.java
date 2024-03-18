package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.repositories.ProdutoServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoAtivo10Reais;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getServicoInativo20Reais;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class ProdutoServicoServiceIntegrationTest {

    @Autowired
    private ProdutoServicoService produtoServicoService;

    @Autowired
    private ProdutoServicoRepository produtoServicoRepository;

    private UUID produtoAtivoId;
    private UUID servicoInativoId;

    @BeforeEach
    void setUp() {
        produtoAtivoId = this.produtoServicoService.save(getProdutoAtivo10Reais()).getId();
        servicoInativoId = this.produtoServicoService.save(getServicoInativo20Reais()).getId();
    }

    @Test
    @DisplayName("Deve retornar página com os resultados ao listar os produtos/serviços")
    void shouldReturnPageWithResultWhenListProductsAndServices() {
        Page<ProdutoServico> result = produtoServicoService.findPage(0, 10, "nome", "ASC",
                null, null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(produtoAtivoId, result.getContent().get(0).getId());
        assertEquals(getProdutoAtivo10Reais().getNome(), result.getContent().get(0).getNome());
        assertEquals(servicoInativoId, result.getContent().get(1).getId());
        assertEquals(getServicoInativo20Reais().getNome(), result.getContent().get(1).getNome());
    }

    @Test
    @DisplayName("Deve retornar página com os resultados ao listar os produtos/serviços com filtro")
    void shouldReturnPageWithResultWhenListProductsAndServicesWithFilter() {
        Page<ProdutoServico> result = produtoServicoService.findPage(0, 10, "nome", "ASC",
                "Serviço 1", null, 19.0, 21.0, null, "SERVICO", false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(servicoInativoId, result.getContent().get(0).getId());
        assertEquals(getServicoInativo20Reais().getNome(), result.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve excluír um produto/serviço quando o id existir")
    public void deleteShouldRemoveResourceWhenIdExists() {
        produtoServicoService.delete(produtoAtivoId);
        assertEquals(1, produtoServicoRepository.count());
    }
}
