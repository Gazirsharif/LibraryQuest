<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вы победили!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f0fff0;
            color: #333;
        }
        h1 {
            color: #28a745;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>Поздравляем, вы победили!</h1>
    <p>Вы успешно завершили квест.</p>
    <a href="${pageContext.request.contextPath}/questEdit">Вернуться к списку квестов</a>
</body>
</html>
