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
        img.centered-image {
            max-width: 80%;
            height: auto;
            display: block;
            margin: 20px auto;
            border: 5px solid #28a745;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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
    <img src="${pageContext.request.contextPath}/images/win.png" alt="Поздравление" class="centered-image">
    <a href="${pageContext.request.contextPath}/questEdit">Вернуться к списку квестов</a>
</body>
</html>
