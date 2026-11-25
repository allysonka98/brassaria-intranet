package br.com.brassaria.intranet.repository;

import br.com.brassaria.intranet.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JpaSpecificationExecutor permite montar consultas com filtros opcionais
 * (ver {@link BebidaSpecs}) que funcionam igual em MySQL, PostgreSQL e H2.
 */
public interface BebidaRepository extends JpaRepository<Bebida, Long>, JpaSpecificationExecutor<Bebida> {

    long countByCategoriaId(Long categoriaId);
}
