package org.projeto.gerenciadortarefa.controller;

import org.projeto.gerenciadortarefa.dto.TarefaDTO;
import org.projeto.gerenciadortarefa.model.Tarefa;
import org.projeto.gerenciadortarefa.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDTO> create(@RequestBody TarefaDTO dto) {
        Tarefa tarefa = dto.toEntity();
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(TarefaDTO.fromEntity(novaTarefa));
    }

    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listarTodas() {
        List<Tarefa> tarefas = tarefaService.listarTodasTarefas();
        List<TarefaDTO> dtos = tarefas.stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> atualizarTarefa(
            @PathVariable Long id,
            @RequestBody TarefaDTO dto) {
        Tarefa tarefa = dto.toEntity();
        Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefa);
        return ResponseEntity.ok(TarefaDTO.fromEntity(tarefaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
