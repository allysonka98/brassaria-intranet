package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Categoria;

public record CategoriaResponse(Long id, String nome, String descricao, int totalBebidas) {

    public static CategoriaResponse de(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome(), c.getDescricao(), c.getBebidas().size());
    }
}
