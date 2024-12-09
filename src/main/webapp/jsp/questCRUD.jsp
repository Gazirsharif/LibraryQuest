<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Квесты</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f9;
            color: #333;
        }

        h1 {
            text-align: center;
            color: #444;
        }

        button {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            margin: 10px 0;
        }

        button:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table th, table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        table th {
            background-color: #007bff;
            color: #fff;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        a {
            color: #007bff;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        a:hover {
            color: #0056b3;
        }

        #form-container {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 20px auto;
        }

        #form-container div {
            margin-bottom: 15px;
        }

        #form-container label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        #form-container input, #form-container textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        #steps-container .step {
            margin-bottom: 20px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }

        #steps-container .step button {
            margin-top: 10px;
        }
    </style>
</head>

<body>
    <h1>Управление квестами</h1>

    <!-- Кнопка добавления -->
    <button onclick="showAddForm()">Добавить квест</button>

    <!-- Таблица квестов -->
    <table>
        <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Описание</th>
            <th>Шаги</th>
            <th>Действия</th>
        </tr>
        <c:forEach var="quest" items="${quests}">
            <tr>
                <td>${quest.questId}</td>
                <td>${quest.title}</td>
                <td>${quest.description}</td>
                <td>
                    <c:forEach var="step" items="${quest.steps}">
                        ${step.question} (${step.options.size()} опций)<br>
                    </c:forEach>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/quests?action=edit&id=${quest.questId}">Редактировать</a>
                    <a href="${pageContext.request.contextPath}/quests?action=delete&id=${quest.questId}"
                        onclick="return confirm('Удалить квест?')">Удалить</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Форма добавления/редактирования -->
    <div id="form-container" style="display:none;">
        <h2>${quest != null ? 'Редактировать квест' : 'Добавить квест'}</h2>

        <form action="${pageContext.request.contextPath}/quests" method="post">
            <input type="hidden" name="action" value="${quest != null ? 'edit' : 'add'}">
            <input type="hidden" name="id" value="${quest.questId}">
            <div>
                <label>Название квеста:</label>
                <input type="text" name="title" value="${quest.title}" required>
            </div>
            <div>
                <label>Описание квеста:</label>
                <textarea name="description" required>${quest.description}</textarea>
            </div>

            <!-- Список шагов -->
            <div id="steps-container">
                <c:forEach var="step" items="${quest.steps}" varStatus="status">
                    <div class="step" data-step-index="${status.index}">
                        <label>Вопрос:</label>
                        <input type="text" name="steps[${status.index}].question" value="${step.question}" required>

                        <!-- Контейнер для опций -->
                        <div class="options-container" data-step-index="${status.index}">
                            <c:forEach var="option" items="${step.options}">
                                <div class="option">
                                    <input type="hidden" name="steps[${status.index}].options.keys" value="${option.key}">
                                    <input type="text" name="steps[${status.index}].options.values" value="${option.value}" required>
                                </div>
                            </c:forEach>
                        </div>

                        <button type="button" onclick="addOption(${status.index})">Добавить опцию</button>
                        <button type="button" onclick="removeStep(${status.index})">Удалить шаг</button>
                    </div>
                </c:forEach>
            </div>
            <button type="button" onclick="addStep()">Добавить шаг</button>

            <button type="submit">Сохранить</button>
        </form>

        <button onclick="hideForm()">Отмена</button>
    </div>

    <script>
        // Показывает форму добавления/редактирования
        function showAddForm() {
            document.getElementById('form-container').style.display = 'block';
        }

        // Скрывает форму
        function hideForm() {
            document.getElementById('form-container').style.display = 'none';
        }

        let stepIndex = 0; // Изначальный индекс для шагов

        // Функция для добавления нового шага
        function addStep() {
            const container = document.getElementById('steps-container');
            const stepDiv = document.createElement('div');
            stepDiv.classList.add('step');
            stepDiv.setAttribute('data-step-index', stepIndex);

            stepDiv.innerHTML = `
        <label>Вопрос:</label>
        <input type="text" name="steps[${stepIndex}].question" required>

        <div class="options-container" data-step-index="${stepIndex}">
            <div class="option">
                <input type="text" name="steps[${stepIndex}].options[0]" placeholder="Опция 1" required>
            </div>
        </div>

        <button type="button" onclick="addOption(${stepIndex})">Добавить опцию</button>
        <button type="button" onclick="removeStep(${stepIndex})">Удалить шаг</button>
    `;

            container.appendChild(stepDiv);
            stepIndex++; // Увеличиваем индекс для следующего шага
        }

        // Функция для добавления новой опции
        function addOption(stepIndex) {
            const container = document.querySelector(`.options-container[data-step-index="${stepIndex}"]`);
            if (container) {
                const optionDiv = document.createElement('div');
                optionDiv.innerHTML = `
            <input type="hidden" name="steps[${stepIndex}].options.keys" value="">
            <input type="text" name="steps[${stepIndex}].options.values" placeholder="Текст опции" required>`;
                container.appendChild(optionDiv);
            }
        }

        // Функция для удаления шага
        function removeStep(stepIndex) {
            const container = document.getElementById('steps-container');
            const stepDiv = document.querySelector(`[data-step-index="${stepIndex}"]`).parentNode;
            if (stepDiv) {
                container.removeChild(stepDiv);
                // Пересчитываем индексы оставшихся шагов
                updateStepIndexes();
            }
        }

        // Функция для обновления индексов шагов после удаления
        function updateStepIndexes() {
            const steps = document.querySelectorAll('#steps-container > div');
            steps.forEach((stepDiv, index) => {
                const stepInputs = stepDiv.querySelectorAll('input');
                stepInputs.forEach(input => {
                    input.name = input.name.replace(/\[\d+\]/, `[${index}]`); // Обновляем индекс
                });
            });
        }

    </script>
</body>

</html>