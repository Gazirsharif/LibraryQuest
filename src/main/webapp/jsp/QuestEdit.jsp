<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Квесты</title>
</head>

<body>
    <h1>Управление квестами</h1>

    <!-- Кнопка добавления -->
    <button onclick="showAddForm()">Добавить квест</button>

    <!-- Таблица квестов -->
    <table border="1">
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
                    <a
                        href="${pageContext.request.contextPath}/questEdit?action=edit&id=${quest.questId}">Редактировать</a>
                    <a href="${pageContext.request.contextPath}/questEdit?action=delete&id=${quest.questId}"
                        onclick="return confirm('Удалить квест?')">Удалить</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Форма добавления/редактирования -->
    <div id="form-container" style="display:none;">
        <h2>${quest != null ? 'Редактировать квест' : 'Добавить квест'}</h2>
        <form action="${pageContext.request.contextPath}/questEdit" method="post">
            <input type="hidden" name="action" value="${quest != null ? 'edit' : 'add'}">
            <input type="hidden" name="id" value="${quest != null ? quest.questId : ''}">
            <input type="hidden" id="deletedSteps" name="deletedSteps">

            <label for="title">Название:</label>
            <input type="text" id="title" name="title" value="${quest != null ? quest.title : ''}" required><br>

            <label for="description">Описание:</label>
            <textarea id="description" name="description"
                required>${quest != null ? quest.description : ''}</textarea><br>

            <label>Шаги:</label>
            <div id="steps-container">
                <c:if test="${quest != null && quest.steps != null}">
                    <c:forEach var="step" items="${quest.steps}">
                        <div class="step-item">
                            <!-- Передаём ID шага -->
                            <input type="hidden" name="stepIds" value="${step.stepId}">
                            <!-- Поле для изменения описания шага -->
                            <input type="text" name="steps" value="${step.question}" required>
                            <!-- Кнопка удаления -->
                            <button type="button" onclick="removeStep(this, '${step.stepId}')">Удалить шаг</button>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
            
            <button type="button" onclick="addStep()">Добавить шаг</button>

            <button type="submit">${quest != null ? 'Сохранить изменения' : 'Добавить'}</button>
        </form>
        <button onclick="hideForm()">Отмена</button>
    </div>

    <c:if test="${not empty message}">
        <p style="color: green;">${message}</p>
    </c:if>

    <script>
        function showAddForm() {
            document.getElementById('form-container').style.display = 'block';
        }

        function hideForm() {
            document.getElementById('form-container').style.display = 'none';
        }

        function addStep() {
            const container = document.getElementById('steps-container');
            const div = document.createElement('div');
            div.innerHTML = '<input type="text" name="steps" required>';
            container.appendChild(div);
        }

        function removeStep(button, stepId) {
            const stepItem = button.closest('.step-item');
            stepItem.remove(); // Удаляем элемент из DOM

            // Если шаг уже существует, добавляем его ID в скрытое поле deletedSteps
            if (stepId) {
                const deletedStepsField = document.getElementById('deletedSteps');
                const deletedSteps = deletedStepsField.value ? deletedStepsField.value.split(',') : [];
                deletedSteps.push(stepId);
                deletedStepsField.value = deletedSteps.join(',');

                console.log('Удалённые шаги:', deletedStepsInput.value); // Отображает удалённые шаги
            }
        }

        // Валидация формы
        document.querySelector('form').addEventListener('submit', function (event) {
            const title = document.getElementById('title').value.trim();
            const description = document.getElementById('description').value.trim();
            if (!title || !description) {
                event.preventDefault(); // Останавливаем отправку формы
                alert('Пожалуйста, заполните название и описание квеста.');
            }

            // Проверяем шаги
            const steps = document.querySelectorAll('#steps-container input');
            for (const step of steps) {
                if (!step.value.trim()) {
                    event.preventDefault();
                    alert('Все шаги должны быть заполнены.');
                    break;
                }
            }
        });
    </script>

</body>

</html>