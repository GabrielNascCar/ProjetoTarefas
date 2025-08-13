package org.projeto.gerenciadortarefa.service;

import jakarta.transaction.Transactional;
import org.projeto.gerenciadortarefa.model.Situacao;
import org.projeto.gerenciadortarefa.model.Tarefa;
import org.projeto.gerenciadortarefa.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setSituacao(Situacao.EM_ANDAMENTO);
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.findAll();
    }
}
