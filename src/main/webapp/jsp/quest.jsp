<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${quest.title}</title>
</head>
<body>
    <h1>${quest.title}</h1>
    <p>${quest.description}</p>

    <h2>Начать квест</h2>
        <a href="<c:url value='/step/${quest.questId}/${firstStep.stepId}' />">Перейти к первому шагу</a>

    <h2>Список квестов</h2>
        <a href="${pageContext.request.contextPath}/questEdit">Перейти к списку квестов</a>
</body>
</html>