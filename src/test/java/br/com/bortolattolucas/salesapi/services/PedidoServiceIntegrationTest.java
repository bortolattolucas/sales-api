package br.com.bortolattolucas.salesapi.services;

import br.com.bortolattolucas.salesapi.domain.ItemPedido;
import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.services.exceptions.DataIntegrityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getProdutoAtivo10Reais;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getServicoAtivo30Reais;
import static br.com.bortolattolucas.salesapi.TestObjectsUtil.getServicoInativo20Reais;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles(value = "test")
@Transactional
public class PedidoServiceIntegrationTest {

    @Autowired
    private ProdutoServicoService produtoServicoService;

    @Autowired
    private PedidoService pedidoService;

    private ProdutoServico produtoAtivo10Reais;
    private ProdutoServico servicoAtivo30Reais;
    private ProdutoServico servicoInativo;

    private UUID pedidoId;

    @BeforeEach
    void setUp() {
        produtoAtivo10Reais = this.produtoServicoService.save(getProdutoAtivo10Reais());
        servicoAtivo30Reais = this.produtoServicoService.save(getServicoAtivo30Reais());
        servicoInativo = this.produtoServicoService.save(getServicoInativo20Reais());

        pedidoId = this.pedidoService.save(Pedido.builder().build()).getId();
    }

    @Test
    @DisplayName("Deve adicionar itens ao pedido e aplicar os valores corretos")
    void shouldReturnPageWithResultWhenListProductsAndServices() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(1.0)
                .build());

        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(servicoAtivo30Reais)
                .quantidade(1.0)
                .build());

        Pedido pedido = this.pedidoService.findById(pedidoId);

        assertNotNull(pedido);
        assertEquals(2, pedido.getItens().size());
        assertEquals(true, pedido.getAberto());
        assertEquals(produtoAtivo10Reais.getPreco(), pedido.getValorTotalProdutos());
        assertEquals(servicoAtivo30Reais.getPreco(), pedido.getValorTotalServicos());
        assertEquals(produtoAtivo10Reais.getPreco() + servicoAtivo30Reais.getPreco(),
                pedido.getValorTotal());
    }

    @Test
    @DisplayName("Deve aplicar percentual de desconto no valor dos PRODUTOS no pedido")
    void shouldApplyDiscountOnProductsValue() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(servicoAtivo30Reais)
                .quantidade(1.0)
                .build());

        this.pedidoService.alterarDescontoProdutos(pedidoId, 10.0);

        Pedido pedido = this.pedidoService.findById(pedidoId);

        assertEquals(20.0, pedido.getValorOriginalProdutos());
        assertEquals(18.0, pedido.getValorTotalProdutos());
        assertEquals(30.0, pedido.getValorTotalServicos());
        assertEquals(48.0, pedido.getValorTotal());
    }

    @Test
    @DisplayName("Não deve ser possível aplicar desconto em um pedido fechado")
    void shouldNotApplyDiscountOnClosedOrder() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        this.pedidoService.fecharPedido(pedidoId);

        Exception exception = assertThrows(DataIntegrityException.class, () ->
            this.pedidoService.alterarDescontoProdutos(pedidoId, 10.0)
        );

        assertEquals("Não é possível alterar o desconto de produtos em pedidos fechados",
                exception.getMessage());
    }

    @Test
    @DisplayName("Não deve ser possível adicionar um item inativo em um pedido")
    void shouldNotAddInactiveItemToOrder() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        Exception exception = assertThrows(DataIntegrityException.class, () ->
            this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                    .produtoServico(servicoInativo)
                    .quantidade(1.0)
                    .build())
        );

        assertEquals("Um pedido não pode ter itens inativos.", exception.getMessage());

        Pedido pedido = this.pedidoService.findById(pedidoId);
        assertEquals(1, pedido.getItens().size());
        assertEquals(produtoAtivo10Reais, pedido.getItens().iterator().next().getProdutoServico());
        assertEquals(20.0, pedido.getValorTotal());
    }

    @Test
    @DisplayName("Não deve ser possível excluir um item de um pedido fechado")
    void shouldNotRemoveItemFromClosedOrder() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        this.pedidoService.fecharPedido(pedidoId);

        Exception exception = assertThrows(DataIntegrityException.class, () ->
            this.pedidoService.deleteItem(pedidoId, produtoAtivo10Reais.getId())
        );

        assertEquals("Não é possível remover itens de pedidos fechados", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve ser possível adicionar o mesmo item mais de uma vez")
    void shouldNotAddTheSameItemMoreThanOnce() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        Exception exception = assertThrows(DataIntegrityException.class, () ->
            this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                    .produtoServico(produtoAtivo10Reais)
                    .quantidade(1.0)
                    .build())
        );

        assertEquals("Não é possível adicionar o mesmo item mais de uma vez", exception.getMessage());

        Pedido pedido = this.pedidoService.findById(pedidoId);
        assertEquals(20.0, pedido.getValorTotal());
    }

    @Test
    @DisplayName("Não deve ser possível adicionar itens a pedidos que não estejam abertos")
    void shouldNotAddItemsToClosedOrder() {
        this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                .produtoServico(produtoAtivo10Reais)
                .quantidade(2.0)
                .build());

        this.pedidoService.fecharPedido(pedidoId);

        Exception exception = assertThrows(DataIntegrityException.class, () ->
            this.pedidoService.adicionarItem(pedidoId, ItemPedido.builder()
                    .produtoServico(servicoAtivo30Reais)
                    .quantidade(1.0)
                    .build())
        );

        assertEquals("Não é possível adicionar itens a pedidos que não estejam abertos", exception.getMessage());

        Pedido pedido = this.pedidoService.findById(pedidoId);
        assertEquals(20.0, pedido.getValorTotal());
    }
}
