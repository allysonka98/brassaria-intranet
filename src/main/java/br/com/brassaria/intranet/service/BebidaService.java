package br.com.brassaria.intranet.service;

import br.com.brassaria.intranet.dto.BebidaRequest;
import br.com.brassaria.intranet.dto.BebidaResponse;
import br.com.brassaria.intranet.exception.RecursoNaoEncontradoException;
import br.com.brassaria.intranet.exception.RegraNegocioException;
import br.com.brassaria.intranet.model.Bebida;
import br.com.brassaria.intranet.model.Evento;
import br.com.brassaria.intranet.repository.BebidaRepository;
import br.com.brassaria.intranet.repository.BebidaSpecs;
import br.com.brassaria.intranet.repository.EventoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BebidaService {

    private static final String COR_PADRAO = "#C8811E";

    private final BebidaRepository bebidaRepository;
    private final EventoRepository eventoRepository;
    private final CategoriaService categoriaService;

    public BebidaService(BebidaRepository bebidaRepository,
                         EventoRepository eventoRepository,
                         CategoriaService categoriaService) {
        this.bebidaRepository = bebidaRepository;
        this.eventoRepository = eventoRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional(readOnly = true)
    public List<BebidaResponse> listar(Long categoriaId, Boolean alcoolica, String nome) {
        Specification<Bebida> filtros = Specification.where(BebidaSpecs.carregarCategoria())
                .and(BebidaSpecs.daCategoria(categoriaId))
                .and(BebidaSpecs.alcoolica(alcoolica))
                .and(BebidaSpecs.nomeContem(nome));
        Sort ordem = Sort.by("categoria.nome").ascending().and(Sort.by("nome").ascending());
        return bebidaRepository.findAll(filtros, ordem).stream()
                .map(BebidaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public BebidaResponse buscarPorId(Long id) {
        return BebidaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public BebidaResponse criar(BebidaRequest dados) {
        Bebida bebida = new Bebida();
        preencher(bebida, dados);
        return BebidaResponse.de(bebidaRepository.save(bebida));
    }

    @Transactional
    public BebidaResponse atualizar(Long id, BebidaRequest dados) {
        Bebida bebida = buscarEntidade(id);
        preencher(bebida, dados);
        return BebidaResponse.de(bebida);
    }

    @Transactional
    public void excluir(Long id) {
        Bebida bebida = buscarEntidade(id);
        // Remove a bebida dos eventos antes de excluir, para não violar a chave estrangeira da tabela N:N.
        for (Evento evento : eventoRepository.findByBebidasServidasId(id)) {
            evento.getBebidasServidas().remove(bebida);
        }
        bebidaRepository.delete(bebida);
    }

    /** Converte uma lista de IDs em entidades, falhando se algum não existir. Usado pelo EventoService. */
    @Transactional(readOnly = true)
    public Set<Bebida> buscarEntidades(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        List<Bebida> encontradas = bebidaRepository.findAllById(ids);
        if (encontradas.size() != new HashSet<>(ids).size()) {
            throw new RegraNegocioException("Uma ou mais bebidas informadas não existem no portfólio.");
        }
        return new HashSet<>(encontradas);
    }

    private Bebida buscarEntidade(Long id) {
        return bebidaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bebida", id));
    }

    private void preencher(Bebida bebida, BebidaRequest dados) {
        validarTeorAlcoolico(dados);
        bebida.setNome(dados.nome().trim());
        bebida.setDescricao(dados.descricao());
        bebida.setCategoria(categoriaService.buscarEntidade(dados.categoriaId()));
        bebida.setAlcoolica(dados.alcoolica());
        bebida.setTeorAlcoolico(dados.teorAlcoolico());
        bebida.setVolumeMl(dados.volumeMl());
        bebida.setCalorias100ml(dados.calorias100ml());
        bebida.setCorHex(dados.corHex() == null ? COR_PADRAO : dados.corHex().toUpperCase());
        bebida.setHarmonizacao(dados.harmonizacao());
    }

    /** Regra de negócio: o teor alcoólico precisa ser coerente com o tipo da bebida. */
    private void validarTeorAlcoolico(BebidaRequest dados) {
        // No Brasil, bebidas com até 0,5% são consideradas "sem álcool".
        if (dados.alcoolica() && dados.teorAlcoolico() <= 0.5) {
            throw new RegraNegocioException("Bebidas alcoólicas devem ter teor alcoólico acima de 0,5%.");
        }
        if (!dados.alcoolica() && dados.teorAlcoolico() > 0.5) {
            throw new RegraNegocioException("Bebidas sem álcool devem ter teor alcoólico de no máximo 0,5%.");
        }
    }
}
