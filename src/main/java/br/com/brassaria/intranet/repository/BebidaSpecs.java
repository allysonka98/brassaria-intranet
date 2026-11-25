package br.com.brassaria.intranet.repository;

import br.com.brassaria.intranet.model.Bebida;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtros do portfólio. Cada método devolve null quando o filtro não foi
 * informado, e o Spring Data simplesmente o ignora ao combinar com and().
 */
public final class BebidaSpecs {

    private BebidaSpecs() {
    }

    /** Traz a categoria na mesma consulta (evita uma consulta extra por bebida). */
    public static Specification<Bebida> carregarCategoria() {
        return (root, query, cb) -> {
            Class<?> tipo = query.getResultType();
            if (tipo != Long.class && tipo != long.class) {
                root.fetch("categoria", JoinType.INNER);
            }
            return null;
        };
    }

    public static Specification<Bebida> daCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Bebida> alcoolica(Boolean alcoolica) {
        if (alcoolica == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("alcoolica"), alcoolica);
    }

    public static Specification<Bebida> nomeContem(String termo) {
        if (termo == null || termo.isBlank()) {
            return null;
        }
        String padrao = "%" + termo.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("nome")), padrao);
    }
}
