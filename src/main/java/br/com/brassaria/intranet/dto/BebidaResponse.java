package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Bebida;

public record BebidaResponse(
        Long id,
        String nome,
        String descricao,
        Long categoriaId,
        String categoriaNome,
        boolean alcoolica,
        Double teorAlcoolico,
        Integer volumeMl,
        Integer calorias100ml,
        String corHex,
        String harmonizacao) {

    public static BebidaResponse de(Bebida b) {
        return new BebidaResponse(b.getId(), b.getNome(), b.getDescricao(),
                b.getCategoria().getId(), b.getCategoria().getNome(),
                b.isAlcoolica(), b.getTeorAlcoolico(), b.getVolumeMl(),
                b.getCalorias100ml(), b.getCorHex(), b.getHarmonizacao());
    }
}
