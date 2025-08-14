# Gerenciador de Tarefas

Um sistema web para gerenciamento de tarefas desenvolvido em *Java 21* com *Spring Boot, utilizando **JPA, **PostgreSQL* e testes unitários para as camadas de controlador e serviço. O projeto também conta com front-end básico em *HTML, **CSS* e *JavaScript* e está preparado para rodar via *Docker*.

---

## Tecnologias utilizadas

- *Back-end*: Java 21, Spring Boot, Spring Data JPA
- *Banco de dados*: PostgreSQL
- *Front-end*: HTML, CSS, JavaScript
- *Contêiner*: Docker
- *Testes*: JUnit e Mockito (camadas Controller e Service)

---

## Funcionalidades

- Criar, atualizar e deletar tarefas
- Listar todas as tarefas ou filtradas por situação (CONCLUIDA / EM_ANDAMENTO) e termos de busca
- Concluir tarefas
- Persistência de dados no PostgreSQL

---

## Estrutura do projeto

- controller – Controladores REST (ex.: TarefaController)
- service – Lógica de negócio (ex.: TarefaService)
- model – Entidades JPA (Tarefa, Situacao)
- dto – Data Transfer Objects (TarefaDTO)
- repository – Repositórios JPA
- util – Classes utilitárias (se houver)

---

## Endpoints da API

Base URL: /api/tarefas

| Método | Endpoint                  | Descrição                                    | Body / Params |
|--------|---------------------------|---------------------------------------------|---------------|
| POST   | /                       | Criar nova tarefa                            | TarefaDTO   |
| GET    | /{id}                   | Buscar tarefa por ID                         | Path Variable |
| GET    | /                       | Listar todas as tarefas                      | -             |
| PUT    | /{id}                   | Atualizar tarefa                             | TarefaDTO   |
| PATCH  | /{id}/concluir          | Concluir tarefa                              | Path Variable |
| GET    | /filtro                 | Listar tarefas filtradas por situação e/ou busca | Query Params situacao, search |
| DELETE | /{id}                   | Deletar tarefa                               | Path Variable |

*Observação*: O endpoint /filtro permite filtrar tarefas pelo status (CONCLUIDA, EM_ANDAMENTO) e/ou pelo termo de busca no título ou responsável.

---


