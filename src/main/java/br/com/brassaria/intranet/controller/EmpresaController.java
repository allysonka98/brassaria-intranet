package br.com.brassaria.intranet.controller;

import br.com.brassaria.intranet.dto.EmpresaRequest;
import br.com.brassaria.intranet.dto.EmpresaResponse;
import br.com.brassaria.intranet.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** Dados institucionais: história, missão, visão e valores. */
@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    public EmpresaResponse obter() {
        return service.obter();
    }

    @PutMapping
    public EmpresaResponse atualizar(@Valid @RequestBody EmpresaRequest dados) {
        return service.salvar(dados);
    }
}
