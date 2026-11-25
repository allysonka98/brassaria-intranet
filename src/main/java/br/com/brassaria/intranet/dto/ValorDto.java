package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Valor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Usado tanto na entrada quanto na saída dos valores da empresa. */
public record ValorDto(
        @NotBlank(message = "O título do valor é obrigatório.")
        @Size(max = 80)
        String titulo,

        @NotBlank(message = "A descrição do valor é obrigatória.")
        @Size(max = 600)
        String descricao) {

    public static ValorDto de(Valor v) {
        return new ValorDto(v.getTitulo(), v.getDescricao());
    }
}
