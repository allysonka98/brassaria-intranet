package br.com.brassaria.intranet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record EmpresaRequest(
        @NotBlank(message = "O nome é obrigatório.") @Size(max = 120)
        String nome,

        @NotNull(message = "O ano de fundação é obrigatório.")
        Integer anoFundacao,

        @Size(max = 200)
        String slogan,

        @Size(max = 3000)
        String historia,

        @NotBlank(message = "A missão é obrigatória.") @Size(max = 1500)
        String missao,

        @NotBlank(message = "A visão é obrigatória.") @Size(max = 1500)
        String visao,

        @NotEmpty(message = "Informe ao menos um valor.")
        List<@Valid ValorDto> valores) {
}
