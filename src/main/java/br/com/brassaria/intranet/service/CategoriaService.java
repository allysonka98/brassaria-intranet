package br.com.brassaria.intranet.service;

import br.com.brassaria.intranet.dto.CategoriaRequest;
import br.com.brassaria.intranet.dto.CategoriaResponse;
import br.com.brassaria.intranet.exception.RecursoNaoEncontradoException;
import br.com.brassaria.intranet.exception.RegraNegocioException;
import br.com.brassaria.intranet.model.Categoria;
import br.com.brassaria.intranet.repository.BebidaRepository;
import br.com.brassaria.intranet.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final BebidaRepository bebidaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, BebidaRepository bebidaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.bebidaRepository = bebidaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAllByOrderByNomeAsc().stream()
                .map(CategoriaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return CategoriaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest dados) {
        if (categoriaRepository.existsByNomeIgnoreCase(dados.nome().trim())) {
            throw new RegraNegocioException("Já existe uma categoria chamada \"" + dados.nome().trim() + "\".");
        }
        Categoria categoria = new Categoria(dados.nome().trim(), dados.descricao());
        return CategoriaResponse.de(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest dados) {
        Categoria categoria = buscarEntidade(id);
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(dados.nome().trim(), id)) {
            throw new RegraNegocioException("Já existe uma categoria chamada \"" + dados.nome().trim() + "\".");
        }
        categoria.setNome(dados.nome().trim());
        categoria.setDescricao(dados.descricao());
        return CategoriaResponse.de(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarEntidade(id);
        if (bebidaRepository.countByCategoriaId(id) > 0) {
            throw new RegraNegocioException("Não é possível excluir uma categoria que possui bebidas cadastradas.");
        }
        categoriaRepository.delete(categoria);
    }

    /** Usado também pelo BebidaService. */
    @Transactional(readOnly = true)
    public Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }
}
