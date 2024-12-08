<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<html>

<head>
    <meta charset="UTF-8">
    <title>Step Editor</title>
</head>

<body>
    <h1>Step Editor</h1>

    <table border="1">
        <tr>
            <th>Quest ID</th>
            <th>Step ID</th>
            <th>Question</th>
            <th>Option Key</th>
            <th>Option Value</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="stepOption" items="${stepOptions}">
            <tr>
                <td>${stepOption.questId}</td>
                <td>${stepOption.stepId}</td>
                <td>${stepOption.question}</td>
                <td>${stepOption.optionKey}</td>
                <td>${stepOption.optionValue}</td>
                <td>
                    <!-- Кнопка редактирования -->
                    <form action="${pageContext.request.contextPath}/stepEdit" method="post">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="questId" value="${stepOption.questId}">
                        <input type="hidden" name="stepId" value="${stepOption.stepId}">
                        <input type="hidden" name="optionKey" value="${stepOption.optionKey}">
                        <input type="submit" value="Edit">
                    </form>

                    <!-- Кнопка удаления -->
                    <form action="${pageContext.request.contextPath}/stepEdit" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="questId" value="${stepOption.questId}">
                        <input type="hidden" name="stepId" value="${stepOption.stepId}">
                        <input type="hidden" name="optionKey" value="${stepOption.optionKey}">
                        <input type="submit" value="Delete">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Форма добавления новой опции -->
    <h2>Add New Option</h2>
    <form action="${pageContext.request.contextPath}/stepEdit" method="post">
        <input type="hidden" name="action" value="add">

        <label for="questId">Quest ID:</label>
        <input type="number" id="questId" name="questId" required>
        <br>

        <label for="stepId">Step ID:</label>
        <input type="number" id="stepId" name="stepId" required>
        <br>

        <label for="optionKey">Option Key:</label>
        <input type="number" id="optionKey" name="optionKey" required>
        <br>

        <label for="optionValue">Option Value:</label>
        <input type="text" id="optionValue" name="optionValue" required>
        <br>

        <input type="submit" value="Add Option">
    </form>
</body>

</html>