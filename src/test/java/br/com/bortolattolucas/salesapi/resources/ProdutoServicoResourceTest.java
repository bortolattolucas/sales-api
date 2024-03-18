package br.com.bortolattolucas.salesapi.resources;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.services.ProdutoServicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.bortolattolucas.salesapi.TestObjectsUtil.BASE_HOST;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoServicoJson;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoServicoJsonInvalido;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoServicoJsonVazio;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoServicoMock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class ProdutoServicoResourceTest {

    private final String BASE_PATH = "/produtos-servicos";
    private final MockMvc mockMvc;

    @MockBean
    private ProdutoServicoService produtoServicoServiceMock;

    @Autowired
    public ProdutoServicoResourceTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Deve retornar status 201 com caminho nas headers ao inserir um novo registro")
    public void shouldReturn201WithPathInHeadersOnInsert() throws Exception {
        Mockito.when(produtoServicoServiceMock.save(Mockito.any(ProdutoServico.class)))
                .thenReturn(getProdutoServicoMock());

        mockMvc.perform(post(BASE_PATH)
                        .content(getProdutoServicoJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", BASE_HOST + BASE_PATH + "/"
                                + getProdutoServicoMock().getId())
                );
    }

    @Test
    @DisplayName("Deve retornar status 400 com validação de campos obrigatórios ao inserir um novo registro vazio")
    public void shouldReturn400WithRequiredFieldsValidationOnInsert() throws Exception {
        mockMvc.perform(post(BASE_PATH).content(getProdutoServicoJsonVazio())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Campo(s) inválido(s) semanticamente"),
                        jsonPath("$.fields.preco").value("Não pode ser vazio"),
                        jsonPath("$.fields.tipo").value("Não pode ser vazio"),
                        jsonPath("$.fields.nome").value("Não pode ser vazio"),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.path").value(BASE_PATH)
                );
    }

    @Test
    @DisplayName("Deve retornar status 400 com validação de campos ao inserir um novo registro inválido")
    public void shouldReturn400WithInvalidFieldsValidationOnInsert() throws Exception {
        mockMvc.perform(post(BASE_PATH).content(getProdutoServicoJsonInvalido())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Campo(s) inválido(s) semanticamente"),
                        jsonPath("$.fields.preco").value("Deve ser um valor positivo"),
                        jsonPath("$.fields.tipo").value("Deve ser PRODUTO ou SERVICO"),
                        jsonPath("$.fields.nome").value("Não pode ser vazio"),
                        jsonPath("$.timestamp").isNotEmpty(),
                        jsonPath("$.path").value(BASE_PATH)
                );
    }
}
