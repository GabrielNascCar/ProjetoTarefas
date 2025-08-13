package org.projeto.gerenciadortarefa.service;

import jakarta.transaction.Transactional;
import org.projeto.gerenciadortarefa.exceptions.TarefaNaoEncontradaException;
import org.projeto.gerenciadortarefa.exceptions.ValidacaoException;
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
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            throw new ValidacaoException("Título é obrigatório");
        }
        tarefa.setSituacao(Situacao.EM_ANDAMENTO);
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return tarefaRepository.findAll();
    }

    @Transactional
    public Tarefa atualizarTarefa(Long id, Tarefa tarefaAtualizada) {
        return tarefaRepository.findById(id)
                .map(existing -> {
                    existing.setTitulo(tarefaAtualizada.getTitulo());
                    existing.setDescricao(tarefaAtualizada.getDescricao());
                    existing.setResponsavel(tarefaAtualizada.getResponsavel());
                    existing.setPrioridade(tarefaAtualizada.getPrioridade());
                    existing.setDeadline(tarefaAtualizada.getDeadline());
                    existing.setSituacao(tarefaAtualizada.getSituacao());
                    return tarefaRepository.save(existing);
                })
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    @Transactional
    public void deletarTarefa(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new TarefaNaoEncontradaException(id);
        }
        tarefaRepository.deleteById(id);
    }

}
