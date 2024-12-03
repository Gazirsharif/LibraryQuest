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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/quests")
public class QuestCRUDServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");


        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // Дефолтное действие
        }

        // Отображение формы для редактирования квеста
        if ("edit".equals(action)) {
            int questId = Integer.parseInt(req.getParameter("id"));
            Quest quest = QuestService.getQuestById(questId);
            req.setAttribute("quest", quest);
            req.getRequestDispatcher("/jsp/questCRUD.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            int questId = Integer.parseInt(req.getParameter("id"));
            QuestService.deleteQuest(questId);
            resp.sendRedirect(req.getContextPath() + "/quests");
            return;
        }

        List<Quest> quests = QuestService.getAllQuests();
        req.setAttribute("quests", quests);
        req.getRequestDispatcher("/jsp/questCRUD.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");


        String action = req.getParameter("action");
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        if ("add".equals(action)) {
            Quest newQuest = new Quest();
            newQuest.setTitle(title);
            newQuest.setDescription(description);

            // Читаем шаги из запроса
            String[] stepDescriptions = req.getParameterValues("steps");
            if (stepDescriptions != null) {
                List<Step> steps = new ArrayList<>();
                for (String stepDesc : stepDescriptions) {
                    steps.add(new Step(newQuest, stepDesc)); // ID будет сгенерирован в базе
                }
                newQuest.setSteps(steps);
            }

            // Сохранение в базу данных через QuestLoader
            QuestService.saveQuest(newQuest);
        } else if ("edit".equals(action)) { // Обработка данных формы для редактирования квеста
            int questId = Integer.parseInt(req.getParameter("id"));
            Quest quest = QuestService.getQuestById(questId);
            quest.setTitle(title);
            quest.setDescription(description);

            // Обновляем шаги
            String[] stepDescriptions = req.getParameterValues("steps");
            if (stepDescriptions != null) {
                List<Step> steps = new ArrayList<>();
                for (String stepDesc : stepDescriptions) {
                    steps.add(new Step(quest, stepDesc));
                }
                quest.setSteps(steps);
            }

            QuestService.saveQuest(quest);
        }

        resp.sendRedirect(req.getContextPath() + "/quests");
    }
}
