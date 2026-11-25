package br.com.brassaria.intranet.repository;

import br.com.brassaria.intranet.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /** A intranet guarda um único registro institucional. */
    Optional<Empresa> findFirstByOrderByIdAsc();
}
