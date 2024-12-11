<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Шаг ${step.stepId} - Квест ${quest.questId}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
            color: #333;
        }

        h1, h2 {
            color: #444;
            text-align: center;
        }

        p {
            text-align: justify;
            margin: 20px auto;
            width: 80%;
            line-height: 1.6;
        }

        ul {
            list-style: none;
            padding: 0;
            margin: 20px auto;
            width: 80%;
        }

        li {
            margin-bottom: 10px;
        }

        .link-container {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 30px;
        }

        a {
            display: inline-block;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #0056b3;
        }

        footer {
            text-align: center;
            margin-top: 50px;
            color: #777;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <h1>Квест ${quest.questId} - Шаг ${step.stepId}</h1>

    <c:choose>
        <c:when test="${step != null}">
            <h2>${step.question}</h2>

            <c:choose>
                <c:when test="${not empty step.options}">
                    <ul>
                        <c:forEach var="option" items="${step.options}">
                            <li>
                                <a href="${pageContext.request.contextPath}/step/${quest.questId}/${option.key}">${option.value}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p>Для данного шага нет доступных опций.</p>
                </c:otherwise>
            </c:choose>

            <div class="link-container">
                <a href="${pageContext.request.contextPath}/quest/${quest.questId}">Вернуться к квесту</a>
            </div>
        </c:when>
        <c:otherwise>
            <p>Шаг не найден</p>
        </c:otherwise>
    </c:choose>

    <footer>
        <p>© 2024 LibraryQuest. Все права защищены.</p>
    </footer>
</body>
</html>