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
            <th>Действия</th>
        </tr>
        <c:forEach var="quest" items="${quests}">
            <tr>
                <td>${quest.questId}</td>
                <td>${quest.title}</td>
                <td>${quest.description}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/quests?action=edit&id=${quest.questId}">Редактировать</a>
                    <a href="${pageContext.request.contextPath}/quests?action=delete&id=${quest.questId}" onclick="return confirm('Удалить квест?')">Удалить</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Форма добавления/редактирования -->
    <div id="form-container" style="display:none;">
        <h2>${quest != null ? 'Редактировать квест' : 'Добавить квест'}</h2>
        <form action="${pageContext.request.contextPath}/quests" method="post">
            <input type="hidden" name="action" value="${quest != null ? 'edit' : 'add'}">
            <input type="hidden" name="id" value="${quest != null ? quest.questId : ''}">

            <label for="title">Название:</label>
            <input type="text" id="title" name="title" value="${quest != null ? quest.title : ''}" required><br>

            <label for="description">Описание:</label>
            <textarea id="description" name="description" required>${quest != null ? quest.description : ''}</textarea><br>

            <label>Шаги:</label>
            <div id="steps-container">
                <c:if test="${quest != null && quest.steps != null}">
                    <c:forEach var="step" items="${quest.steps}">
                        <div>
                            <input type="text" name="steps" value="${step.description}" required>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
            <button type="button" onclick="addStep()">Добавить шаг</button><br>

            <button type="submit">${quest != null ? 'Сохранить изменения' : 'Добавить'}</button>
        </form>
        <button onclick="hideForm()">Отмена</button>
    </div>

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
    </script>
</body>
</html>
