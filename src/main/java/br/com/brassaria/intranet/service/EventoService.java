package br.com.brassaria.intranet.service;

import br.com.brassaria.intranet.dto.EventoRequest;
import br.com.brassaria.intranet.dto.EventoResponse;
import br.com.brassaria.intranet.exception.RecursoNaoEncontradoException;
import br.com.brassaria.intranet.model.Evento;
import br.com.brassaria.intranet.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final BebidaService bebidaService;

    public EventoService(EventoRepository eventoRepository, BebidaService bebidaService) {
        this.eventoRepository = eventoRepository;
        this.bebidaService = bebidaService;
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listar() {
        return eventoRepository.findAllByOrderByDataEventoDesc().stream()
                .map(EventoResponse::de)
                .toList();
    }

    /** Regra do site: a tela inicial mostra os 3 últimos eventos cadastrados. */
    @Transactional(readOnly = true)
    public List<EventoResponse> listarRecentes() {
        return eventoRepository.findTop3ByOrderByDataCadastroDescIdDesc().stream()
                .map(EventoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventoResponse buscarPorId(Long id) {
        return EventoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public EventoResponse criar(EventoRequest dados) {
        Evento evento = new Evento();
        preencher(evento, dados);
        return EventoResponse.de(eventoRepository.save(evento));
    }

    @Transactional
    public EventoResponse atualizar(Long id, EventoRequest dados) {
        Evento evento = buscarEntidade(id);
        preencher(evento, dados);
        return EventoResponse.de(evento);
    }

    @Transactional
    public void excluir(Long id) {
        eventoRepository.delete(buscarEntidade(id));
    }

    private Evento buscarEntidade(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento", id));
    }

    private void preencher(Evento evento, EventoRequest dados) {
        evento.setTitulo(dados.titulo().trim());
        evento.setResumo(dados.resumo().trim());
        evento.setConteudo(dados.conteudo().trim());
        evento.setLocal(dados.local().trim());
        evento.setDataEvento(dados.dataEvento());
        evento.setBebidasServidas(bebidaService.buscarEntidades(dados.bebidaIds()));
    }
}
