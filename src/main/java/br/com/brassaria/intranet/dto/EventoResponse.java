package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Evento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EventoResponse(
        Long id,
        String titulo,
        String resumo,
        String conteudo,
        String local,
        LocalDate dataEvento,
        LocalDateTime dataCadastro,
        List<BebidaResumoResponse> bebidasServidas) {

    public static EventoResponse de(Evento e) {
        List<BebidaResumoResponse> bebidas = e.getBebidasServidas().stream()
                .map(BebidaResumoResponse::de)
                .toList();
        return new EventoResponse(e.getId(), e.getTitulo(), e.getResumo(), e.getConteudo(),
                e.getLocal(), e.getDataEvento(), e.getDataCadastro(), bebidas);
    }
}
