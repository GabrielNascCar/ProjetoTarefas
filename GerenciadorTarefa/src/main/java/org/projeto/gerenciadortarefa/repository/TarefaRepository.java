package org.projeto.gerenciadortarefa.repository;

import org.projeto.gerenciadortarefa.model.Situacao;
import org.projeto.gerenciadortarefa.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findBySituacao(Situacao situacao);
}
