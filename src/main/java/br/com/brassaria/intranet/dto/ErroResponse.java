package br.com.brassaria.intranet.dto;

import java.time.LocalDateTime;
import java.util.Map;

/** Formato padrão das respostas de erro da API. */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        Map<String, String> campos) {
}
