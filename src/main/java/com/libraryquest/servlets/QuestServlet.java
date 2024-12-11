package com.libraryquest.servlets;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Общий сервлет для обработки запросов к квестам. Динамически определяет, какой квест загружать на основе URL.
 */
@WebServlet("/quest/*")
public class QuestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Путь в ссылке
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Если ID не указан, возвращаем список всех квестов
            req.setAttribute("quests", QuestService.getAllQuests());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        //Деление пути
        String[] pathParts = pathInfo.split("/");

        if (pathParts.length == 2) { //Если указан только квест
            try {
                // Логика загрузки квеста по ID
                int questId = Integer.parseInt(pathInfo.substring(1)); // Получение ID квеста из URL
                Quest quest = QuestService.getQuestById(questId);

                if (quest == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Квест не найден");
                    return;
                }

                List<Step> steps = quest.getSteps();
                if (steps == null) {
                    throw new IllegalStateException("У квеста списка шагов");
                }

                Step firstStep = steps
                        .stream()
                        .min(Comparator.comparing(Step::getStepId))
                        .orElseThrow(() -> new IllegalStateException("У квеста нет шагов"));

                // Передача данных в JSP
                req.setAttribute("quest", quest);
                req.setAttribute("firstStep", firstStep);
                req.getRequestDispatcher("/jsp/quest.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Идентификаторы должны быть числовыми");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный запрос");
        }
    }
}