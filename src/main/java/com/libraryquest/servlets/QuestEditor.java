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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/questEdit")
public class QuestEditor extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            req.getRequestDispatcher("/jsp/QuestEdit.jsp").forward(req, resp);
            return; //TODO: Что меняет?
        } else if ("delete".equals(action)) {
            int questId = Integer.parseInt(req.getParameter("id"));
            Quest quest = QuestService.getQuestById(questId);
            QuestService.deleteQuest(quest);
            resp.sendRedirect(req.getContextPath() + "/questEdit");
            return;
        }

        List<Quest> quests = QuestService.getAllQuests();

        Map<Quest, Set<Step>> questSteps = quests
                .stream()
                .collect(Collectors.toMap(quest -> quest,
                        quest -> new LinkedHashSet<>(quest.getSteps())));

        req.setAttribute("questSteps", questSteps);
        req.setAttribute("quests", quests);
        req.getRequestDispatcher("/jsp/QuestEdit.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            newQuest.setSteps(extractSteps(req, newQuest));

            QuestService.saveQuest(newQuest);
        } else if ("edit".equals(action)) {
            int questId = Integer.parseInt(req.getParameter("id"));
            Quest quest = QuestService.getQuestById(questId);

            quest.setTitle(title);
            quest.setDescription(description);
            quest.setSteps(extractSteps(req, quest));

            // Удаление шагов
            String deletedStepsParam = req.getParameter("deletedSteps");
//            System.out.println("Удалённые шаги: " + deletedStepsParam);

            if (deletedStepsParam != null && !deletedStepsParam.isEmpty()) {
                String[] deletedStepIds = deletedStepsParam.split(",");
                for (String stepId : deletedStepIds) {
                    try {
                        int stpId = Integer.parseInt(stepId);

                        // Передать ID квеста и шага в метод удаления
                        QuestService.deleteStepById(questId, stpId);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            QuestService.saveQuest(quest);
        }

        resp.sendRedirect(req.getContextPath() + "/questEdit");
    }

    public List<Step> extractSteps(HttpServletRequest req, Quest quest) {
        String[] stepIds = req.getParameterValues("stepIds"); // IDs шагов из формы (если они есть)
        String[] stepDescriptions = req.getParameterValues("steps"); // Описания шагов
        List<Step> steps = new ArrayList<>();

        if (stepDescriptions != null) {
            for (int i = 0; i < stepDescriptions.length; i++) {
                String stepDesc = stepDescriptions[i];
                Step step;

                // Если шаг имеет ID, то это существующий шаг
                if (stepIds != null && i < stepIds.length && !stepIds[i].isEmpty()) {
                    int stepId = Integer.parseInt(stepIds[i]);
                    step = quest.getSteps().stream()
                            .filter(s -> s.getStepId() == stepId)
                            .findFirst()
                            .orElse(new Step(quest, stepDesc)); // Создаем новый, если не нашли
                    step.setQuestion(stepDesc); // Обновляем описание
                } else {
                    // Это новый шаг
                    step = new Step(quest, stepDesc);
                }

                steps.add(step);
            }
        }
        return steps;
    }
}