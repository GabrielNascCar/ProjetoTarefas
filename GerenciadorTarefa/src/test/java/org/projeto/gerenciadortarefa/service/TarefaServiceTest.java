package org.projeto.gerenciadortarefa.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.projeto.gerenciadortarefa.exceptions.TarefaNaoEncontradaException;
import org.projeto.gerenciadortarefa.model.*;
import org.projeto.gerenciadortarefa.repository.TarefaRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    @Test
    void criarTarefa_DeveRetornarTarefaSalva() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Teste");
        tarefa.setPrioridade(Prioridade.ALTA);
        tarefa.setDeadline(LocalDate.now().plusDays(1));

        when(repository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa resultado = service.criarTarefa(tarefa);

        assertNotNull(resultado);
        assertEquals(Situacao.EM_ANDAMENTO, resultado.getSituacao());
        verify(repository, times(1)).save(tarefa);
    }

    @Test
    void atualizarTarefa_QuandoExistir_DeveRetornarTarefaAtualizada() {
        Long id = 1L;
        Tarefa existente = new Tarefa();
        existente.setId(id);

        Tarefa atualizada = new Tarefa();
        atualizada.setTitulo("Novo Título");

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Tarefa.class))).thenReturn(existente);

        Tarefa resultado = service.atualizarTarefa(id, atualizada);

        assertEquals("Novo Título", resultado.getTitulo());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void atualizarTarefa_QuandoNaoExistir_DeveLancarExcecao() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () ->
                service.atualizarTarefa(id, new Tarefa()));
    }

    @Test
    void deletarTarefa_QuandoExistir_DeveChamarDelete() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.deletarTarefa(id);

        verify(repository, times(1)).deleteById(id);
    }
}