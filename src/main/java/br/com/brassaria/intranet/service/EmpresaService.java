package br.com.brassaria.intranet.service;

import br.com.brassaria.intranet.dto.EmpresaRequest;
import br.com.brassaria.intranet.dto.EmpresaResponse;
import br.com.brassaria.intranet.exception.RecursoNaoEncontradoException;
import br.com.brassaria.intranet.model.Empresa;
import br.com.brassaria.intranet.model.Valor;
import br.com.brassaria.intranet.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public EmpresaResponse obter() {
        return EmpresaResponse.de(buscarEntidade());
    }

    /** Atualiza (ou cria, se ainda não existir) os dados institucionais. */
    @Transactional
    public EmpresaResponse salvar(EmpresaRequest dados) {
        Empresa empresa = empresaRepository.findFirstByOrderByIdAsc().orElseGet(Empresa::new);
        empresa.setNome(dados.nome().trim());
        empresa.setAnoFundacao(dados.anoFundacao());
        empresa.setSlogan(dados.slogan());
        empresa.setHistoria(dados.historia());
        empresa.setMissao(dados.missao().trim());
        empresa.setVisao(dados.visao().trim());

        empresa.limparValores();
        dados.valores().forEach(v -> empresa.adicionarValor(new Valor(v.titulo().trim(), v.descricao().trim())));

        return EmpresaResponse.de(empresaRepository.save(empresa));
    }

    private Empresa buscarEntidade() {
        return empresaRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dados institucionais ainda não cadastrados."));
    }
}
