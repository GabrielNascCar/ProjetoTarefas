package org.projeto.gerenciadortarefa.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GerenciadorTarefasException extends RuntimeException {
    private final HttpStatus status;

    public GerenciadorTarefasException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
