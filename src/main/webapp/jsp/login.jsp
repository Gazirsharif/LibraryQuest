<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход в LibraryQuest</title>
    <link href="css/styles.css" rel="stylesheet">
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
    <div class="container">
        <h1>Вход в LibraryQuest</h1>

        <c:if test="${not empty param.logoutMessage}">
            <p class="success-message">${param.logoutMessage}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Войти</button>
        </form>

        <p>Еще нет аккаунта? <a href="${pageContext.request.contextPath}/register">Зарегистрируйтесь</a></p>

        <c:if test="${not empty requestScope.error}">
            <p class="error-message">${requestScope.error}</p>
        </c:if>
    </div>
</body>
</html>
