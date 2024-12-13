<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добро пожаловать в LibraryQuest</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(to bottom, #f4f4f9, #eaeaea);
            color: #333;
            text-align: center;
        }

        header {
            background-color: #4CAF50;
            color: white;
            padding: 20px 0;
        }

        header h1 {
            margin: 0;
            font-size: 2.5rem;
        }

        p {
            font-size: 1.2rem;
            margin: 20px 0;
        }

        a {
            display: inline-block;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 1rem;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #0056b3;
        }

        main {
            padding: 20px;
        }

        footer {
            margin-top: 20px;
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            font-size: 0.9rem;
        }
    </style>
<body>
    <header>
        <h1>Добро пожаловать в LibraryQuest!</h1>
    </header>
    <main>
        <c:if test="${not empty username}">
            <p>Добро пожаловать, ${username}!</p>
        </c:if>

        <p>Это интерактивная библиотека текстовых квестов. Выберите квест из списка или создайте свой!</p>
        <a href="${pageContext.request.contextPath}/questEdit">Перейти к списку квестов</a>
    </main>
    <footer>
        &copy; 2024 LibraryQuest. Все права защищены.
    </footer>
</body>

</html>