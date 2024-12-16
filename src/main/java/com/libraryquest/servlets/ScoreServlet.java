package com.libraryquest.servlets;

import com.libraryquest.models.Score;
import com.libraryquest.services.QuestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет для отображения таблицы счета
 */
@WebServlet("/score")
public class ScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Получаем данные из сервиса
        List<Score> scores = QuestService.getAllScores();

        // Передаем данные в JSP
        req.setAttribute("scores", scores);

        // Перенаправляем на JSP
        req.getRequestDispatcher("/jsp/score.jsp").forward(req, resp);
    }
}
