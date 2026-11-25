package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Bebida;

/** Versão enxuta da bebida, usada dentro da resposta de eventos. */
public record BebidaResumoResponse(Long id, String nome, String corHex) {

    public static BebidaResumoResponse de(Bebida b) {
        return new BebidaResumoResponse(b.getId(), b.getNome(), b.getCorHex());
    }
}
