package br.com.brassaria.intranet.controller;

import br.com.brassaria.intranet.dto.EventoRequest;
import br.com.brassaria.intranet.dto.EventoResponse;
import br.com.brassaria.intranet.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventoResponse> listar() {
        return service.listar();
    }

    /** Três últimos eventos cadastrados — consumido pela tela inicial. */
    @GetMapping("/recentes")
    public List<EventoResponse> recentes() {
        return service.listarRecentes();
    }

    @GetMapping("/{id}")
    public EventoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<EventoResponse> criar(@Valid @RequestBody EventoRequest dados) {
        EventoResponse criado = service.criar(dados);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    public EventoResponse atualizar(@PathVariable Long id, @Valid @RequestBody EventoRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
