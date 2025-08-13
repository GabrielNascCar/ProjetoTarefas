package org.projeto.gerenciadortarefa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.projeto.gerenciadortarefa.dto.TarefaDTO;
import org.projeto.gerenciadortarefa.model.*;
import org.projeto.gerenciadortarefa.service.TarefaService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TarefaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long ID_VALIDO = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tarefaController).build();
    }

    @Test
    void criarTarefa_DeveRetornar201() throws Exception {

        TarefaDTO dto = new TarefaDTO();
        dto.setTitulo("Teste Controller");
        dto.setPrioridade(Prioridade.ALTA);

        Tarefa tarefaSalva = new Tarefa();
        tarefaSalva.setId(ID_VALIDO);
        tarefaSalva.setTitulo(dto.getTitulo());

        when(tarefaService.criarTarefa(any(Tarefa.class))).thenReturn(tarefaSalva);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID_VALIDO));
    }

    @Test
    void listarTodas_DeveRetornarLista() throws Exception {

        Tarefa tarefa = new Tarefa();
        tarefa.setId(ID_VALIDO);
        tarefa.setTitulo("Tarefa Teste");

        when(tarefaService.listarTodasTarefas()).thenReturn(List.of(tarefa));

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Tarefa Teste"));
    }

    @Test
    void atualizarTarefa_DeveRetornarTarefaAtualizada() throws Exception {

        TarefaDTO dto = new TarefaDTO();
        dto.setTitulo("Título Atualizado");

        Tarefa tarefaAtualizada = new Tarefa();
        tarefaAtualizada.setId(ID_VALIDO);
        tarefaAtualizada.setTitulo(dto.getTitulo());

        when(tarefaService.atualizarTarefa(eq(ID_VALIDO), any(Tarefa.class)))
                .thenReturn(tarefaAtualizada);

        mockMvc.perform(put("/tarefas/{id}", ID_VALIDO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título Atualizado"));
    }

    @Test
    void deletarTarefa_DeveRetornar204() throws Exception {

        doNothing().when(tarefaService).deletarTarefa(ID_VALIDO);
        
        mockMvc.perform(delete("/tarefas/{id}", ID_VALIDO))
                .andExpect(status().isNoContent());

        verify(tarefaService, times(1)).deletarTarefa(ID_VALIDO);
    }
}