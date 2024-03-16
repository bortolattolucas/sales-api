package br.com.bortolattolucas.salesapi.resources;

import br.com.bortolattolucas.salesapi.domain.Pedido;
import br.com.bortolattolucas.salesapi.dto.PedidoDto;
import br.com.bortolattolucas.salesapi.mapper.PedidoMapper;
import br.com.bortolattolucas.salesapi.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoResource(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody(required = false) PedidoDto pedidoDto) {
        Pedido pedido = pedidoService.save(PedidoMapper.toEntity(pedidoDto));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

//    @GetMapping(value = "/{id}")
//    public ResponseEntity<ProdutoServicoDto> read(@PathVariable String id) {
//        ProdutoServicoDto produtoServicoDto =
//                ProdutoServicoMapper.toDto(produtoServicoService.findById(UUID.fromString(id)));
//
//        return ResponseEntity.ok().body(produtoServicoDto);
//    }

//    @GetMapping
//    public ResponseEntity<Page<ProdutoServicoDto>> list(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "20") int size,
//            @RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
//            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
//            @RequestParam(name = "nome", required = false) String nome,
//            @RequestParam(name = "preco", required = false) Double preco,
//            @RequestParam(name = "precoMinimo", required = false) Double precoMinimo,
//            @RequestParam(name = "precoMaximo", required = false) Double precoMaximo,
//            @RequestParam(name = "descricao", required = false) String descricao,
//            @RequestParam(name = "tipo", required = false) String tipo,
//            @RequestParam(name = "ativo", required = false) Boolean ativo
//    ) {
//        Page<ProdutoServico> produtosServicosPage = produtoServicoService.findPage(page, size, orderBy, direction, nome,
//                preco, precoMinimo, precoMaximo, descricao, tipo, ativo);
//
//        return ResponseEntity.ok().body(produtosServicosPage.map(ProdutoServicoMapper::toDto));
//    }

//    @PatchMapping(value = "/{id}")
//    public ResponseEntity<Void> updatePartial(@PathVariable UUID id,
//                                              @RequestBody @Valid ProdutoServicoPatchDto produtoServicoPatchDto) {
//        produtoServicoService.updatePartial(id, ProdutoServicoMapper.toEntity(produtoServicoPatchDto));
//
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
