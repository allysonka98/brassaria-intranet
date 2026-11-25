package br.com.brassaria.intranet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record EventoRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
        String titulo,

        @NotBlank(message = "O resumo é obrigatório.")
        @Size(max = 300, message = "O resumo deve ter no máximo 300 caracteres.")
        String resumo,

        @NotBlank(message = "O conteúdo da notícia é obrigatório.")
        @Size(max = 5000, message = "O conteúdo deve ter no máximo 5000 caracteres.")
        String conteudo,

        @NotBlank(message = "O local é obrigatório.")
        @Size(max = 150, message = "O local deve ter no máximo 150 caracteres.")
        String local,

        @NotNull(message = "A data do evento é obrigatória.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataEvento,

        /** IDs das bebidas servidas no evento (opcional). */
        Set<Long> bebidaIds) {
}
