package br.com.brassaria.intranet.exception;

/** Lançada quando uma regra de negócio é violada (HTTP 422). */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
