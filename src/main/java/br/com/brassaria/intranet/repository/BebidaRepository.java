package br.com.brassaria.intranet.repository;

import br.com.brassaria.intranet.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BebidaRepository extends JpaRepository<Bebida, Long> {

    /**
     * Busca com filtros opcionais: cada parâmetro nulo é ignorado.
     * O "join fetch" já traz a categoria na mesma consulta.
     */
    @Query("""
            select b from Bebida b join fetch b.categoria c
            where (:categoriaId is null or c.id = :categoriaId)
              and (:alcoolica is null or b.alcoolica = :alcoolica)
              and (:nome is null or lower(b.nome) like lower(concat('%', :nome, '%')))
            order by c.nome asc, b.nome asc
            """)
    List<Bebida> buscar(@Param("categoriaId") Long categoriaId,
                        @Param("alcoolica") Boolean alcoolica,
                        @Param("nome") String nome);

    long countByCategoriaId(Long categoriaId);
}
