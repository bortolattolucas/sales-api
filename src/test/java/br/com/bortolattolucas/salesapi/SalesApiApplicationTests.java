package br.com.bortolattolucas.salesapi;

import br.com.bortolattolucas.salesapi.resources.PedidoResource;
import br.com.bortolattolucas.salesapi.resources.ProdutoServicoResource;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import br.com.bortolattolucas.salesapi.services.ProdutoServicoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
class SalesApiApplicationTests {

    private final ProdutoServicoResource produtoServicoResource;
    private final ProdutoServicoService produtoServicoService;
    private final PedidoResource pedidoResource;
    private final PedidoService pedidoService;

    @Autowired
    public SalesApiApplicationTests(ProdutoServicoResource produtoServicoResource,
                                    ProdutoServicoService produtoServicoService,
                                    PedidoResource pedidoResource,
                                    PedidoService pedidoService) {
        this.produtoServicoResource = produtoServicoResource;
        this.produtoServicoService = produtoServicoService;
        this.pedidoResource = pedidoResource;
        this.pedidoService = pedidoService;
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(produtoServicoResource).isNotNull();
        Assertions.assertThat(produtoServicoService).isNotNull();
        Assertions.assertThat(pedidoResource).isNotNull();
        Assertions.assertThat(pedidoService).isNotNull();
    }

}
