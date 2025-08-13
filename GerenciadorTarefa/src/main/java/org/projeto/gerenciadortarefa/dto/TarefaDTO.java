package org.projeto.gerenciadortarefa.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.projeto.gerenciadortarefa.model.Prioridade;
import org.projeto.gerenciadortarefa.model.Situacao;
import org.projeto.gerenciadortarefa.model.Tarefa;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String titulo;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "Responsável é obrigatório")
    @Size(max = 100, message = "Responsável deve ter no máximo 100 caracteres")
    private String responsavel;

    @NotNull(message = "Prioridade é obrigatória")
    private Prioridade prioridade;

    @NotNull(message = "Deadline é obrigatória")
    @FutureOrPresent(message = "Deadline deve ser hoje ou no futuro")
    private LocalDate deadline;

    private Situacao situacao;

    public Tarefa toEntity() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(this.id);
        tarefa.setTitulo(this.titulo);
        tarefa.setDescricao(this.descricao);
        tarefa.setResponsavel(this.responsavel);
        tarefa.setPrioridade(this.prioridade);
        tarefa.setDeadline(this.deadline);
        tarefa.setSituacao(this.situacao != null ? this.situacao : Situacao.EM_ANDAMENTO);
        return tarefa;
    }

    public static TarefaDTO fromEntity(Tarefa tarefa) {
        TarefaDTO dto = new TarefaDTO();
        dto.setId(tarefa.getId());
        dto.setTitulo(tarefa.getTitulo());
        dto.setDescricao(tarefa.getDescricao());
        dto.setResponsavel(tarefa.getResponsavel());
        dto.setPrioridade(tarefa.getPrioridade());
        dto.setDeadline(tarefa.getDeadline());
        dto.setSituacao(tarefa.getSituacao());
        return dto;
    }
}
