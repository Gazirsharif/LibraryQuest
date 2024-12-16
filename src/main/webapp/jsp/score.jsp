<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.libraryquest.models.Score" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Таблица счета</title>
    <link rel="stylesheet" href="css/scoreStyle.css">
</head>
<body>
    <div style="text-align:center; margin: 20px;">
        <a href="${pageContext.request.contextPath}">Перейти к начальной странице</a> |
        <a href="${pageContext.request.contextPath}/questEdit">Перейти к списку квестов</a> |
        <a href="${pageContext.request.contextPath}/stepEdit">Перейти к списку шагов</a>
    </div>

    <h1>Таблица счета</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Quest ID</th>
                <th>Quest Title</th>
                <th>User ID</th>
                <th>Username</th>
                <th>Wins</th>
                <th>Losses</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Score> scores = (List<Score>) request.getAttribute("scores");
                if (scores != null && !scores.isEmpty()) {
                    for (Score score : scores) {
            %>
            <tr>
                <td><%= score.getQuestId() %></td>
                <td><%= score.getQuestTitle() %></td>
                <td><%= score.getUserId() %></td>
                <td><%= score.getUserName() %></td>
                <td><%= score.getWin() %></td>
                <td><%= score.getLose() %></td>
            </tr>
            <%   }
                } else { %>
            <tr>
                <td colspan="6" style="text-align: center;">Данные отсутствуют</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
