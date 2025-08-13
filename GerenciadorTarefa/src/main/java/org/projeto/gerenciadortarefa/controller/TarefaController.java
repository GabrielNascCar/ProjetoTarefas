package org.projeto.gerenciadortarefa.controller;

import org.projeto.gerenciadortarefa.model.Tarefa;
import org.projeto.gerenciadortarefa.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<Tarefa> create(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }
}
