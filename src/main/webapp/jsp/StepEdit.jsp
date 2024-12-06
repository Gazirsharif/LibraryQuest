<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<html>

<head>
    <meta charset="UTF-8">
    <title>Step Editor</title>
</head>

<body>
    <h1>Step Editor</h1>

    <table border="1">
        <tr>
            <th>Quest ID</th>
            <th>Step ID</th>
            <th>Question</th>
            <th>Option Key</th>
            <th>Option Value</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="stepOption" items="${stepOptions}">
            <tr>
                <td>${stepOption.questId}</td>
                <td>${stepOption.stepId}</td>
                <td>${stepOption.question}</td>
                <td>${stepOption.optionKey}</td>
                <td>${stepOption.optionValue}</td>
                <td>
                    <form action="/stepEdit" method="post">
                        <input type="hidden" name="stepId" value="${stepOption.stepId}">
                        <input type="hidden" name="optionKey" value="${stepOption.optionKey}">
                        <input type="submit" name="action" value="Edit">
                        <input type="submit" name="action" value="Delete">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="/stepEdit?action=add">Add New Option</a>
</body>

</html>