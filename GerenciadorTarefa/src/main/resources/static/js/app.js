document.addEventListener('DOMContentLoaded', function() {
    const API_BASE_URL = 'http://localhost:8080/api/tarefas';
    const taskForm = document.getElementById('taskForm');
    const taskList = document.getElementById('taskList');
    const filterStatus = document.getElementById('filterStatus');
    const searchInput = document.getElementById('searchInput');

    loadTasks();

    taskForm.addEventListener('submit', function(e) {
        e.preventDefault();
        createTask();
    });

    filterStatus.addEventListener('change', loadTasks);
    searchInput.addEventListener('input', loadTasks);

    async function loadTasks() {
        const situacao = filterStatus.value;
        const search = searchInput.value.trim().toLowerCase();

        try {
            const params = new URLSearchParams();
            if (situacao && situacao !== 'TODAS') params.append('situacao', situacao);
            if (search) params.append('search', search);

            const url = `${API_BASE_URL}/filtro?${params.toString()}`;

            const response = await fetch(url, {
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Erro ao carregar tarefas: ${errorText}`);
            }

            const tasks = await response.json();
            renderTasks(tasks);
        } catch (error) {
            console.error('Erro:', error);
            taskList.innerHTML = `<p class="error-message">${error.message || 'Erro ao carregar tarefas'}</p>`;
        }
    }

    function renderTasks(tasks) {
        taskList.innerHTML = '';

        if (!tasks || tasks.length === 0) {
            taskList.innerHTML = '<p class="no-tasks">Nenhuma tarefa encontrada</p>';
            return;
        }

        tasks.forEach(task => {
            const taskCard = document.createElement('div');
            taskCard.className = `task-card ${task.situacao === 'CONCLUIDA' ? 'completed' : ''}`;
            taskCard.dataset.id = task.id;

            const priorityClass = getPriorityClass(task.prioridade);

            taskCard.innerHTML = `
                <div>
                    <h3>${task.titulo}</h3>
                    <p>${task.descricao || 'Sem descrição'}</p>
                    <div class="task-meta">
                        <span><strong>Responsável:</strong> ${task.responsavel}</span>
                        <span class="${priorityClass}"><strong>Prioridade:</strong> ${formatPriority(task.prioridade)}</span>
                        <span><strong>Prazo:</strong> ${task.deadline ? formatDate(task.deadline) : 'Sem prazo'}</span>
                    </div>
                </div>
                <div class="task-actions">
                    ${task.situacao === 'EM_ANDAMENTO' ?
                `<button onclick="completeTask(${task.id})" class="btn-complete">✓ Concluir</button>` : ''}
                    <button onclick="editTask(${task.id})" class="btn-edit">✎ Editar</button>
                    <button onclick="deleteTask(${task.id})" class="btn-delete">🗑 Excluir</button>
                </div>
            `;

            taskList.appendChild(taskCard);
        });
    }

    async function createTask() {
        const titulo = document.getElementById('titulo').value.trim();
        const descricao = document.getElementById('descricao').value.trim();
        const responsavel = document.getElementById('responsavel').value.trim();
        const prioridade = document.getElementById('prioridade').value;
        const deadline = document.getElementById('deadline').value;

        if (!titulo || !responsavel || !prioridade) {
            alert('Preencha todos os campos obrigatórios (Título, Responsável e Prioridade)');
            return;
        }

        const taskData = {
            titulo: titulo,
            descricao: descricao || null,
            responsavel: responsavel,
            prioridade: prioridade,
            deadline: deadline || null,
            situacao: 'EM_ANDAMENTO'
        };

        try {
            const response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Erro ao criar tarefa');
            }

            taskForm.reset();
            await loadTasks();
            alert('Tarefa criada com sucesso!');
        } catch (error) {
            console.error('Erro:', error);
            alert(error.message || 'Erro ao criar tarefa');
        }
    }

    function formatPriority(priority) {
        const priorities = {
            'ALTA': 'Alta',
            'MEDIA': 'Média',
            'BAIXA': 'Baixa'
        };
        return priorities[priority] || priority;
    }

    function formatDate(dateString) {
        if (!dateString) return 'Sem prazo';
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return 'Data inválida';

        const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
        return date.toLocaleDateString('pt-BR', options);
    }

    function getPriorityClass(priority) {
        return priority ? `priority-${priority.toLowerCase()}` : 'priority-none';
    }
});

async function completeTask(id) {
    if (!confirm('Deseja marcar esta tarefa como concluída?')) return;

    try {
        const response = await fetch(`http://localhost:8080/api/tarefas/${id}/concluir`, {
            method: 'PATCH'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao concluir tarefa');
        }

        location.reload();
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao concluir tarefa');
    }
}

async function deleteTask(id) {
    if (!confirm('Tem certeza que deseja excluir esta tarefa permanentemente?')) return;

    try {
        const response = await fetch(`http://localhost:8080/api/tarefas/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao excluir tarefa');
        }

        location.reload();
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao excluir tarefa');
    }
}

function editTask(id) {

    console.log('Editar tarefa', id);

    alert(`Funcionalidade de edição para tarefa ${id} será implementada aqui`);
}