package br.com.brassaria.intranet.dto;

import jakarta.validation.constraints.*;

public record BebidaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
        String descricao,

        @NotNull(message = "Informe a categoria.")
        Long categoriaId,

        @NotNull(message = "Informe se a bebida é alcoólica.")
        Boolean alcoolica,

        @NotNull(message = "Informe o teor alcoólico (0 para sem álcool).")
        @DecimalMin(value = "0.0", message = "O teor alcoólico não pode ser negativo.")
        @DecimalMax(value = "20.0", message = "O teor alcoólico deve ser no máximo 20%.")
        Double teorAlcoolico,

        @NotNull(message = "Informe o volume em ml.")
        @Positive(message = "O volume deve ser maior que zero.")
        Integer volumeMl,

        @PositiveOrZero(message = "As calorias não podem ser negativas.")
        Integer calorias100ml,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "A cor deve estar no formato #RRGGBB.")
        String corHex,

        @Size(max = 300, message = "A harmonização deve ter no máximo 300 caracteres.")
        String harmonizacao) {
}
