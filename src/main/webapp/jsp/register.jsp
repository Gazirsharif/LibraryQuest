<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация в LibraryQuest</title>
    <link href="css/styles.css" rel="stylesheet">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <script src="<c:url value=" /static/jquery-3.6.0.min.js" />"></script>

</head>
<body>
    <div class="container">
        <h1>Регистрация в LibraryQuest</h1>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Зарегистрироваться</button>
        </form>

        <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войдите</a></p>
    </div>
</body>
</html>