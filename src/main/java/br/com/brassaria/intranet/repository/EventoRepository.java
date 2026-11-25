package br.com.brassaria.intranet.repository;

import br.com.brassaria.intranet.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    /** Os três últimos eventos cadastrados (exibidos na tela inicial). */
    List<Evento> findTop3ByOrderByDataCadastroDescIdDesc();

    List<Evento> findAllByOrderByDataEventoDesc();

    List<Evento> findByBebidasServidasId(Long bebidaId);
}
