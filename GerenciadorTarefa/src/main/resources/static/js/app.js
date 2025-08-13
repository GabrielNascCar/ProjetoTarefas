const API_BASE_URL = 'http://localhost:8080/api/tarefas';

let loadTasks;

document.addEventListener('DOMContentLoaded', function() {
    const taskForm = document.getElementById('taskForm');
    const taskList = document.getElementById('taskList');
    const filterStatus = document.getElementById('filterStatus');
    const searchInput = document.getElementById('searchInput');

    loadTasks = async function() {
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
    };
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

    loadTasks();
    taskForm.addEventListener('submit', function(e) {
        e.preventDefault();
        createTask();
    });
    filterStatus.addEventListener('change', loadTasks);
    searchInput.addEventListener('input', loadTasks);
});

async function completeTask(id) {
    if (!confirm('Deseja marcar esta tarefa como concluída?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}/concluir`, {
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
        const response = await fetch(`${API_BASE_URL}/${id}`, {
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

async function editTask(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`);

        if (!response.ok) {
            throw new Error('Erro ao carregar tarefa para edição');
        }

        const task = await response.json();

        document.getElementById('taskId').value = task.id;
        document.getElementById('titulo').value = task.titulo;
        document.getElementById('descricao').value = task.descricao || '';
        document.getElementById('responsavel').value = task.responsavel;
        document.getElementById('prioridade').value = task.prioridade;
        document.getElementById('deadline').value = task.deadline ? task.deadline.split('T')[0] : '';

        enableEditMode();
        document.querySelector('.card').scrollIntoView({ behavior: 'smooth' });

    } catch (error) {
        console.error('Erro ao editar tarefa:', error);
        alert('Erro ao carregar tarefa: ' + error.message);
    }
}

function enableEditMode() {
    const form = document.getElementById('taskForm');
    form.classList.add('editing-mode');

    const submitButton = form.querySelector('button[type="submit"]');
    submitButton.textContent = 'Atualizar Tarefa';
    submitButton.classList.add('btn-update');

    const cancelButton = document.createElement('button');
    cancelButton.type = 'button';
    cancelButton.textContent = 'Cancelar';
    cancelButton.classList.add('btn-cancel');
    cancelButton.onclick = cancelEdit;

    const formRow = form.querySelector('.form-row');
    formRow.appendChild(cancelButton);

    const taskId = document.getElementById('taskId').value;
    document.querySelector(`.task-card[data-id="${taskId}"]`)?.classList.add('editing');
}

function cancelEdit() {
    const form = document.getElementById('taskForm');
    form.reset();
    form.classList.remove('editing-mode');

    const submitButton = form.querySelector('button[type="submit"]');
    submitButton.textContent = 'Salvar Tarefa';
    submitButton.classList.remove('btn-update');

    const cancelButton = form.querySelector('.btn-cancel');
    if (cancelButton) {
        cancelButton.remove();
    }

    const taskId = document.getElementById('taskId').value;
    document.querySelector(`.task-card[data-id="${taskId}"]`)?.classList.remove('editing');
}

async function updateTask(id) {
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
        deadline: deadline || null
    };

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Erro ao atualizar tarefa');
        }

        cancelEdit();
        await loadTasks();
        alert('Tarefa atualizada com sucesso!');
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao atualizar tarefa');
    }
}