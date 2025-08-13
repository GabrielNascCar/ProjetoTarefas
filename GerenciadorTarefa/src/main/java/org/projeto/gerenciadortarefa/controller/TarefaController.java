package org.projeto.gerenciadortarefa.controller;

import org.projeto.gerenciadortarefa.dto.TarefaDTO;
import org.projeto.gerenciadortarefa.model.Situacao;
import org.projeto.gerenciadortarefa.model.Tarefa;
import org.projeto.gerenciadortarefa.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDTO> create(@RequestBody TarefaDTO dto) {
        Tarefa tarefa = dto.toEntity();
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(TarefaDTO.fromEntity(novaTarefa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> getTarefaPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(TarefaDTO.fromEntity(tarefa));
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

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirTarefa(@PathVariable Long id) {
        tarefaService.concluirTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<TarefaDTO>> listarFiltradas(
            @RequestParam(required = false) String situacao,
            @RequestParam(required = false) String search) {

        List<Tarefa> tarefas;

        if ("CONCLUIDA".equalsIgnoreCase(situacao)) {
            tarefas = tarefaService.listarPorSituacao(Situacao.CONCLUIDA);
        } else if ("EM_ANDAMENTO".equalsIgnoreCase(situacao)) {
            tarefas = tarefaService.listarPorSituacao(Situacao.EM_ANDAMENTO);
        } else {
            tarefas = tarefaService.listarTodasTarefas();
        }

        if (search != null && !search.isEmpty()) {
            final String termo = search.toLowerCase();
            tarefas = tarefas.stream()
                    .filter(t -> t.getTitulo().toLowerCase().contains(termo) ||
                            t.getResponsavel().toLowerCase().contains(termo))
                    .collect(Collectors.toList());
        }

        List<TarefaDTO> dtos = tarefas.stream()
                .map(TarefaDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
