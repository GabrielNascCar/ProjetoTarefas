package org.projeto.gerenciadortarefa.exceptions;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends GerenciadorTarefasException {
    public ValidacaoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
