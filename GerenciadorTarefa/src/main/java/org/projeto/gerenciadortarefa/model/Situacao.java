package org.projeto.gerenciadortarefa.model;

import lombok.Getter;

@Getter
public enum Situacao {
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída");

    private final String descricao;

    Situacao(String descricao) {
        this.descricao = descricao;
    }
}
