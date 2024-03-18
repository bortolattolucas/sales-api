package br.com.bortolattolucas.salesapi.resources;

import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.dto.ItemPedidoDto;
import br.com.bortolattolucas.salesapi.dto.PedidoDto;
import br.com.bortolattolucas.salesapi.dto.PedidoPatchDto;
import br.com.bortolattolucas.salesapi.mapper.PedidoMapper;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Tag(name = "Pedidos", description = "API de Pedidos")
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoResource(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Cria um novo Pedido")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody(required = false) PedidoDto pedidoDto) {
        Pedido pedido = pedidoService.save(PedidoMapper.toEntity(pedidoDto));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Busca um Pedido pelo ID")
    @Transactional
    @GetMapping(value = "/{id}")
    public ResponseEntity<PedidoDto> read(@PathVariable String id) {
        PedidoDto pedidoDto =
                PedidoMapper.toDto(pedidoService.findById(UUID.fromString(id)));

        return ResponseEntity.ok().body(pedidoDto);
    }

    @Operation(summary = "Lista os Pedidos paginados com filtros opcionais")
    @Transactional
    @GetMapping
    public ResponseEntity<Page<PedidoDto>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "direction", defaultValue = "DESC") String direction,
            @RequestParam(name = "valorTotal", required = false) Double valorTotal,
            @RequestParam(name = "valorTotalMinimo", required = false) Double valorTotalMinimo,
            @RequestParam(name = "valorTotalMaximo", required = false) Double valorTotalMaximo,
            @RequestParam(name = "aberto", required = false) Boolean aberto,
            @RequestParam(name = "createdAt", required = false) LocalDateTime createdAt,
            @RequestParam(name = "createdAtMin", required = false) LocalDateTime createdAtMin,
            @RequestParam(name = "createdAtMax", required = false) LocalDateTime createdAtMax
    ) {
        Page<Pedido> pedidosPage = pedidoService.findPage(page, size, orderBy, direction, valorTotal,
                valorTotalMinimo, valorTotalMaximo, aberto, createdAt, createdAtMin, createdAtMax);

        return ResponseEntity.ok().body(pedidosPage.map(PedidoMapper::toDto));
    }

    @Operation(summary = "Adiciona um Item ao Pedido pelo ID")
    @PostMapping(value = "/{id}/itens")
    public ResponseEntity<Void> adicionarItem(@PathVariable UUID id,
                                              @RequestBody ItemPedidoDto itemPedidoDto) {
        pedidoService.adicionarItem(id, PedidoMapper.toEntity(itemPedidoDto));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Fecha um Pedido pelo ID")
    @PatchMapping(value = "/{id}/fechar")
    public ResponseEntity<Void> fecharPedido(@PathVariable UUID id) {
        pedidoService.fecharPedido(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Altera a porcentagem de desconto dos produtos de um Pedido pelo ID")
    @PatchMapping(value = "/{id}/desconto-produtos")
    public ResponseEntity<Void> alterarDescontoProdutos(@PathVariable UUID id,
                                                        @RequestBody @Valid PedidoPatchDto pedidoPatchDto) {
        pedidoService.alterarDescontoProdutos(id, pedidoPatchDto.getPrcDescontoProdutos());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um Pedido pelo ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um Item do Pedido pelo ID do Pedido e ID do Produto/Serviço")
    @DeleteMapping(value = "/{id}/itens/{idProdutoServico}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id,
                                           @PathVariable UUID idProdutoServico) {
        pedidoService.deleteItem(id, idProdutoServico);
        return ResponseEntity.noContent().build();
    }
}
