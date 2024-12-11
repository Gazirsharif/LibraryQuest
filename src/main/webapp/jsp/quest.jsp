<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${quest.title}</title>
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
    <h1>${quest.title}</h1>
    <p>${quest.description}</p>

    <div class="link-container">
        <a href="<c:url value='/step/${quest.questId}/${firstStep.stepId}' />">Перейти к первому шагу</a>
        <a href="${pageContext.request.contextPath}/questEdit">Перейти к списку квестов</a>
    </div>

    <footer>
        <p>© 2024 LibraryQuest. Все права защищены.</p>
    </footer>
</body>
</html>
