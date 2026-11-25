package br.com.brassaria.intranet.exception;

/** Lançada quando um registro buscado por ID não existe (HTTP 404). */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado(a).");
    }

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
