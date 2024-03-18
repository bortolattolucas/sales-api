package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.repositories.ProdutoServicoRepository;
import br.com.bortolattolucas.salesapi.services.impl.ProdutoServicoServiceImpl;
import com.querydsl.core.BooleanBuilder;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoAtivo10Reais;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoServicoMock;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getServicoInativo20Reais;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProdutoServicoServiceImplTest {

    @InjectMocks
    private ProdutoServicoServiceImpl produtoServicoService;

    @Mock
    private ProdutoServicoRepository produtoServicoRepository;

    private int page;
    private int size;
    private String orderBy;
    private String direction;

    @BeforeEach
    void setUp() {
        page = 0;
        size = 10;
        orderBy = "nome";
        direction = "ASC";

        Page<ProdutoServico> mockPage = new PageImpl<>(List.of(getProdutoAtivo10Reais(), getServicoInativo20Reais()));

        when(produtoServicoRepository.findAll(any(BooleanBuilder.class), any(PageRequest.class))).thenReturn(mockPage);

        when(produtoServicoRepository.findById(getProdutoServicoMock().getId()))
                .thenThrow(new ObjectNotFoundException(getProdutoServicoMock().getId(), "Not found"));
    }

    @Test
    @DisplayName("Deve retornar página com os resultados ao listar os produtos/serviços")
    void shouldReturnPageWithResultWhenListProductsAndServices() {
        Page<ProdutoServico> result = produtoServicoService.findPage(page, size, orderBy, direction,
                null, null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(getProdutoAtivo10Reais().getId(), result.getContent().get(0).getId());
        assertEquals(getProdutoAtivo10Reais().getNome(), result.getContent().get(0).getNome());
        assertEquals(getServicoInativo20Reais().getId(), result.getContent().get(1).getId());
        assertEquals(getServicoInativo20Reais().getNome(), result.getContent().get(1).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao chamar métodos persistentes com objeto não encontrado")
    void shouldThrowExceptionWhenCallingPersistingMethodsWithObjectNotFound() {
        assertThrows(ObjectNotFoundException.class, () -> produtoServicoService
                .update(getProdutoServicoMock().getId(), getProdutoServicoMock()));

        assertThrows(ObjectNotFoundException.class, () -> produtoServicoService
                .updatePartial(getProdutoServicoMock().getId(), getProdutoServicoMock()));

        assertThrows(ObjectNotFoundException.class, () -> produtoServicoService
                .delete(getProdutoServicoMock().getId()));

        verify(produtoServicoRepository, times(0)).save(any());
        verify(produtoServicoRepository, times(0)).deleteById(any());
    }

}
