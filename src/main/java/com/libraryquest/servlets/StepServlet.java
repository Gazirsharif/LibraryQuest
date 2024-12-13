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

/**
 * Сервлет для обработки конкретных шагов, включая переход на следующий шаг в зависимости от ответа пользователя
 */
@WebServlet("/step/*")
public class StepServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Путь в ссылке
        String pathInfo = req.getPathInfo();

        if (pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный маршрут для шагов");
            return;
        }

        // Разделение пути для получения questId и stepId
        String[] pathParts = pathInfo.substring(1).split("/");
        if (pathParts.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный маршрут для шагов, путь должен быть вида questId/stepId");
            return;
        }

        try {
            int questId = Integer.parseInt(pathParts[0]);
            int stepId = Integer.parseInt(pathParts[1]);

            if (stepId == -2) {
                // Передача данных в JSP для отображения
                req.getRequestDispatcher("/jsp/lose.jsp").forward(req, resp);
            } else if (stepId == -3) {
                // Передача данных в JSP для отображения
                req.getRequestDispatcher("/jsp/win.jsp").forward(req, resp);
            }

            // Получение текущего шага
            Step step = QuestService.getStepContent(questId, stepId);

            if (step == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Шаг не найден");
                return;
            }

            // Передача объекта квеста
            Quest quest = step.getQuest();

            req.setAttribute("quest", quest);
            req.setAttribute("step", step);

            // Передача данных в JSP для отображения
            req.getRequestDispatcher("/jsp/step.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
//            System.err.println("Некорректный формат чисел в маршруте: " + Arrays.toString(pathParts));
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Идентификаторы должны быть числовыми");
        }
    }
}
