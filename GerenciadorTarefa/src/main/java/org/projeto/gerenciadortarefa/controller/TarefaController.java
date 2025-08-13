package org.projeto.gerenciadortarefa.controller;

import org.projeto.gerenciadortarefa.model.Tarefa;
import org.projeto.gerenciadortarefa.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        List<Tarefa> tarefas = tarefaService.listarTodasTarefas();
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(
            @PathVariable Long id,
            @RequestBody Tarefa tarefaAtualizada) {
        Tarefa tarefa = tarefaService.atualizarTarefa(id, tarefaAtualizada);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
