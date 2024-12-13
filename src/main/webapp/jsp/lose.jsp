<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вы проиграли</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #fff0f0;
            color: #333;
        }
        h1 {
            color: #dc3545;
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
    <h1>К сожалению, вы проиграли</h1>
    <p>Попробуйте еще раз и добейтесь успеха!</p>
    <a href="${pageContext.request.contextPath}/questEdit">Вернуться к списку квестов</a>
</body>
</html>