<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Step Editor</title>
    <style>

        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f9;
            color: #333;
        }

        h1, h2 {
            color: #444;
        }

        a {
            color: #0066cc;
            text-decoration: none;
            margin-right: 15px;
        }

        a:hover {
            text-decoration: underline;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        table, th, td {
            border: 1px solid #ccc;
        }

        th {
            background-color: #f0f0f0;
            padding: 10px;
            text-align: left;
        }

        td {
            padding: 10px;
        }

        form {
            display: inline-block;
            margin: 0;
        }

        form input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
        }

        form input[type="submit"]:hover {
            background-color: #45a049;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 10px;
        }

        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 8px;
            margin: 5px 0 10px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            background-color: #007BFF;
            color: white;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 4px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>

<body>
    <header>
        <h1>Управление шагами</h1>
    </header>


    <div style="text-align:center; margin: 20px;">
        <a href="${pageContext.request.contextPath}">Перейти к начальной странице</a> |
        <a href="${pageContext.request.contextPath}/questEdit">Перейти к списку квестов</a> |
        <a href="${pageContext.request.contextPath}/score">Перейти к общему счету</a>
    </div>

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
                    <form action="${pageContext.request.contextPath}/stepEdit" method="get">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="questId" value="${stepOption.questId}">
                        <input type="hidden" name="stepId" value="${stepOption.stepId}">
                        <input type="hidden" name="optionKey" value="${stepOption.optionKey}">
                        <input type="hidden" name="optionValue" value="${stepOption.optionValue}">
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

    <!-- Форма редактирования, видна только при action="edit" -->
    <c:if test="${not empty editOption}">
        <h2>Edit Option</h2>
        <form action="${pageContext.request.contextPath}/stepEdit" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="questId" value="${editOption.questId}">
            <input type="hidden" name="stepId" value="${editOption.stepId}">
            <input type="hidden" name="optionKey" value="${editOption.optionKey}">

            <label for="newOptionKey">New Option Key:</label>
            <input type="number" id="newOptionKey" name="newOptionKey" value="${editOption.optionKey}" required>
            <br>

            <label for="newOptionValue">New Option Value:</label>
            <input type="text" id="newOptionValue" name="optionValue" value="${editOption.optionValue}" required>
            <br>

            <input type="submit" value="Update Option">
        </form>
    </c:if>
</body>
</html>