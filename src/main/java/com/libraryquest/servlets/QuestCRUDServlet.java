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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/quests")
public class QuestCRUDServlet extends HttpServlet {
    //TODO: По сути класс был первым шаблоном для операции CRUD. Из за сложности логики был разбит на QuestEdit и StepEdit

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // Действие по умолчанию - список квестов
        }

        try {
            switch (action) {
                case "edit" -> handleEditAction(req, resp);
                case "delete" -> handleDeleteAction(req, resp);
                default -> handleListAction(req, resp);
            }
        } catch (NumberFormatException e) {
            // Обработка ошибок парсинга ID
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quest ID.");
        } catch (Exception e) {
            // Общая обработка ошибок
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            e.printStackTrace();
        }
    }

    // Обработка действия "edit"
    private void handleEditAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int questId = Integer.parseInt(req.getParameter("id"));
        Quest quest = QuestService.getQuestById(questId);

        if (quest == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quest not found.");
            return;
        }

        req.setAttribute("quest", quest);
        req.getRequestDispatcher("/jsp/questCRUD.jsp").forward(req, resp);
    }

    // Обработка действия "delete"
    private void handleDeleteAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int questId = Integer.parseInt(req.getParameter("id"));
        Quest quest = QuestService.getQuestById(questId);

        QuestService.deleteQuest(quest);
        resp.sendRedirect(req.getContextPath() + "/quests");
    }

    // Обработка действия "list" (по умолчанию)
    private void handleListAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Quest> quests = QuestService.getAllQuests();
        req.setAttribute("quests", quests);
        req.getRequestDispatcher("/jsp/questCRUD.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Читаем параметры из запроса
        String action = req.getParameter("action");
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        Quest quest = null;

        if ("add".equals(action)) {
            // Создаём новый квест
            quest = new Quest();
            quest.setTitle(title);
            quest.setDescription(description);
        } else if ("edit".equals(action)) {
            // Редактируем существующий квест
            int questId = Integer.parseInt(req.getParameter("id"));
            quest = QuestService.getQuestById(questId);

            if (quest != null) {
                quest.setTitle(title);
                quest.setDescription(description);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Quest not found.");
                return;
            }
        }


        if (quest != null) {
            // Парсинг шагов из запроса
            List<Step> steps = new ArrayList<>();
            int stepIndex = 0; // Индекс текущего шага
            while (true) {
                // Попытка извлечь данные следующего шага
                String question = req.getParameter("steps[" + stepIndex + "].question");
                if (question == null) {

                    req.getParameterMap().forEach((key, value) -> {
                        System.out.println("Параметр: " + key + " Значение: " + String.join(", ", value));
                    });

                    break; // Если больше шагов нет, выходим из цикла
                }

                // Извлекаем опции для шага
                String[] optionKeys = req.getParameterValues("steps[" + stepIndex + "].options.keys");
                String[] optionValues = req.getParameterValues("steps[" + stepIndex + "].options.values");

                Map<Integer, String> options = new HashMap<>();
                if (optionKeys != null && optionValues != null) {
                    for (int j = 0; j < optionKeys.length; j++) {
                        options.put(Integer.parseInt(optionKeys[j]), optionValues[j]);
                    }
                }

                // Создаём новый шаг и добавляем его к квесту
                Step step = new Step(quest, question);
                step.setOptions(options);
                steps.add(step);

                stepIndex++;
            }

            quest.setSteps(steps);

            // Сохраняем квест
            QuestService.saveQuest(quest);
        }

        resp.sendRedirect(req.getContextPath() + "/quests");
    }
}
