package org.projeto.gerenciadortarefa.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GerenciadorTarefasException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(GerenciadorTarefasException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Ocorreu um erro inesperado"));
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {
        private final String message;
    }
}
