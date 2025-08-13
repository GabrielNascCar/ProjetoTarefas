package org.projeto.gerenciadortarefa.model;

import lombok.Getter;

@Getter
public enum Prioridade {
    ALTA("Alta"),
    MEDIA("Média"),
    BAIXA("Baixa");

    private final String descricao;

    Prioridade(String descricao) {
        this.descricao = descricao;
    }
}