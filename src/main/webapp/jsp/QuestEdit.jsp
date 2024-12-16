<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ru">

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

            p {
                font-size: 1.2rem;
                margin: 20px 0;
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
    <header>
        <h1>Управление квестами</h1>
    </header>

    <div class="content">
        <div style="text-align:center; margin: 20px;">
            <a href="${pageContext.request.contextPath}">Перейти к начальной странице</a> |
            <a href="${pageContext.request.contextPath}/stepEdit">Перейти к списку шагов</a> |
            <a href="${pageContext.request.contextPath}/score">Перейти к общему счету</a>
        </div>

        <div style="text-align:center; margin: 20px;">
            <p>Возможные действия на странице:</p>
            <ul style="text-align: left; margin: 20px auto; max-width: 600px;">
                <li>Начать квест: Нажмите на название квеста в списке, вас перекинет на страницу квеста.</li>
                <li>Создать квест: Воспользуйтесь редактором для создания собственного сюжета.</li>
                <li>Продолжить начатое: Вернитесь к квестам или шагам которые вы уже начали, редактируйте,
                 добавляйте и удаляйте в любое удобное время</li>
            </ul>
        </div>



        <button onclick="showAddForm()">Добавить квест</button>

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
                    <td><a href="${pageContext.request.contextPath}/quest/${quest.questId}">${quest.title}</a></td>
                    <td>${quest.description}</td>
                    <td>
                        <c:forEach var="step" items="${questSteps[quest.questId]}">
                            ${step.question} (${step.options.size()} опций)<br>
                        </c:forEach>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/questEdit?action=edit&id=${quest.questId}">Редактировать</a>
                        <a href="${pageContext.request.contextPath}/questEdit?action=delete&id=${quest.questId}"
                           onclick="return confirm('Удалить квест?')">Удалить</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <div id="form-container" style="display:none;">
        <h2>${quest != null ? 'Редактировать квест' : 'Добавить квест'}</h2>
        <form action="${pageContext.request.contextPath}/questEdit" method="post">
            <input type="hidden" name="action" value="${quest != null ? 'edit' : 'add'}">
            <input type="hidden" name="id" value="${quest != null ? quest.questId : ''}">
            <input type="hidden" id="deletedSteps" name="deletedSteps">

            <label for="title">Название:</label>
            <input type="text" id="title" name="title" value="${quest != null ? quest.title : ''}" required>

            <label for="description">Описание:</label>
            <textarea id="description" name="description" required>${quest != null ? quest.description : ''}</textarea>

            <label>Шаги:</label>
            <div id="steps-container">
                <c:if test="${quest != null && quest.steps != null}">
                    <c:forEach var="step" items="${steps}">
                        <div class="step-item">
                            <input type="hidden" name="stepIds" value="${step.stepId}">
                            <input type="text" name="steps" value="${step.question}" required>
                            <button type="button" onclick="removeStep(this, '${step.stepId}')">Удалить шаг</button>
                        </div>
                    </c:forEach>
                </c:if>
            </div>

            <button type="button" onclick="addStep()">Добавить шаг</button>
            <button type="submit">${quest != null ? 'Сохранить изменения' : 'Добавить'}</button>
            <button type="button" onclick="hideForm()">Отмена</button>
        </form>
    </div>

    <c:if test="${not empty message}">
        <p class="message">${message}</p>
    </c:if>

    <footer>
        <p>© 2024 LibraryQuest. Все права защищены.</p>
    </footer>

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