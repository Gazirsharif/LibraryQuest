<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Шаг ${step.stepId} - Квест ${quest.questId}</title>
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
                    <p>
                        <a href="${pageContext.request.contextPath}/quest/${quest.questId}">
                            Вернуться к квесту
                        </a>
                    </p>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <p>Шаг не найден</p>
        </c:otherwise>
    </c:choose>
</body>

</html>