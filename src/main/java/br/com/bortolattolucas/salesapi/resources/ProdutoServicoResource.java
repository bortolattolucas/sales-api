package br.com.bortolattolucas.salesapi.resources;

import br.com.bortolattolucas.salesapi.domain.ProdutoServico;
import br.com.bortolattolucas.salesapi.dto.ProdutoServicoDto;
import br.com.bortolattolucas.salesapi.dto.ProdutoServicoPatchDto;
import br.com.bortolattolucas.salesapi.mapper.ProdutoServicoMapper;
import br.com.bortolattolucas.salesapi.services.ProdutoServicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;

import java.net.URI;
import java.util.UUID;

@RestController
@Tag(name = "Produto/Serviço", description = "API de Produtos/Serviços")
@RequestMapping(value = "/produtos-servicos")
public class ProdutoServicoResource {

    private final ProdutoServicoService produtoServicoService;

    @Autowired
    public ProdutoServicoResource(ProdutoServicoService produtoServicoService) {
        this.produtoServicoService = produtoServicoService;
    }

    @Operation(summary = "Cria um novo Produto/Serviço")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ProdutoServicoDto produtoServicoDto) {
        ProdutoServico produtoServico = produtoServicoService.save(ProdutoServicoMapper.toEntity(produtoServicoDto));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(produtoServico.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Busca um Produto/Serviço pelo ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoServicoDto> read(@PathVariable String id) {
        ProdutoServicoDto produtoServicoDto =
                ProdutoServicoMapper.toDto(produtoServicoService.findById(UUID.fromString(id)));

        return ResponseEntity.ok().body(produtoServicoDto);
    }

    @Operation(summary = "Lista os Produtos/Serviços paginados com filtros opcionais")
    @GetMapping
    public ResponseEntity<Page<ProdutoServicoDto>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "preco", required = false) Double preco,
            @RequestParam(name = "precoMinimo", required = false) Double precoMinimo,
            @RequestParam(name = "precoMaximo", required = false) Double precoMaximo,
            @RequestParam(name = "descricao", required = false) String descricao,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "ativo", required = false) Boolean ativo
    ) {
        Page<ProdutoServico> produtosServicosPage = produtoServicoService.findPage(page, size, orderBy, direction, nome,
                preco, precoMinimo, precoMaximo, descricao, tipo, ativo);

        return ResponseEntity.ok().body(produtosServicosPage.map(ProdutoServicoMapper::toDto));
    }

    @Operation(summary = "Atualiza um Produto/Serviço pelo ID")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id,
                                       @RequestBody @Valid ProdutoServicoDto produtoServicoDto) {
        produtoServicoService.update(id, ProdutoServicoMapper.toEntity(produtoServicoDto));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza parcialmente um Produto/Serviço pelo ID")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Void> updatePartial(@PathVariable UUID id,
                                              @RequestBody @Valid ProdutoServicoPatchDto produtoServicoPatchDto) {
        produtoServicoService.updatePartial(id, ProdutoServicoMapper.toEntity(produtoServicoPatchDto));

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um Produto/Serviço pelo ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        produtoServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
