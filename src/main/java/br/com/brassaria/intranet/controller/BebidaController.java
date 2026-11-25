package br.com.brassaria.intranet.controller;

import br.com.brassaria.intranet.dto.BebidaRequest;
import br.com.brassaria.intranet.dto.BebidaResponse;
import br.com.brassaria.intranet.service.BebidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bebidas")
public class BebidaController {

    private final BebidaService service;

    public BebidaController(BebidaService service) {
        this.service = service;
    }

    /** Ex.: GET /api/bebidas?categoriaId=1&alcoolica=false&nome=lager */
    @GetMapping
    public List<BebidaResponse> listar(@RequestParam(required = false) Long categoriaId,
                                       @RequestParam(required = false) Boolean alcoolica,
                                       @RequestParam(required = false) String nome) {
        return service.listar(categoriaId, alcoolica, nome);
    }

    @GetMapping("/{id}")
    public BebidaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<BebidaResponse> criar(@Valid @RequestBody BebidaRequest dados) {
        BebidaResponse criada = service.criar(dados);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{id}")
    public BebidaResponse atualizar(@PathVariable Long id, @Valid @RequestBody BebidaRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
