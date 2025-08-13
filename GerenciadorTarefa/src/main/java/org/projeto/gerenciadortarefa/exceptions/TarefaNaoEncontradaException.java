package org.projeto.gerenciadortarefa.exceptions;

import org.springframework.http.HttpStatus;

public class TarefaNaoEncontradaException extends GerenciadorTarefasException {
  public TarefaNaoEncontradaException(Long id) {
    super("Tarefa com ID " + id + " não encontrada", HttpStatus.NOT_FOUND);
  }
}
