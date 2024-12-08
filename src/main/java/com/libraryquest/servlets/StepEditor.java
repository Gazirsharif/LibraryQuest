package com.libraryquest.servlets;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.models.StepOption;
import com.libraryquest.services.QuestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/stepEdit")
public class StepEditor extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        List<Quest> quests = QuestService.getAllQuests();

//        List<StepOption> stepOptions = quests
//                .stream()
//                .flatMap(quest -> quest.getSteps().stream())
//                .flatMap(step -> step.getOptions().entrySet().stream()
//                        .map(entry -> new StepOption(step.getQuest().getQuestId(), step.getStepId(), step.getQuestion(), entry.getKey(), entry.getValue())))
//                .collect(Collectors.toSet())
//                .stream()
//                .toList();

        // Создаем список StepOption, добавляя шаги с опциями и шаги без них
        List<StepOption> stepOptions = quests.stream()
                .flatMap(quest -> quest.getSteps().stream())
                .flatMap(step -> {
                    // Если у шага есть опции, создаем StepOption для каждой опции
                    if (step.getOptions() != null && !step.getOptions().isEmpty()) {
                        return step.getOptions().entrySet().stream()
                                .map(entry -> new StepOption(
                                        step.getQuest().getQuestId(),
                                        step.getStepId(),
                                        step.getQuestion(),
                                        entry.getKey(),
                                        entry.getValue()
                                ));
                    } else {
                        // Если у шага нет опций, создаем "пустой" StepOption
                        return Stream.of(new StepOption(
                                step.getQuest().getQuestId(),
                                step.getStepId(),
                                step.getQuestion(),
                                -1, // Условный ключ для пустой опции
                                "No options available" // Условное значение
                        ));
                    }
                })
                .collect(Collectors.toSet())
                .stream()
                .toList();

        // Печатаем результат для отладки
        stepOptions.forEach(System.out::println);

        req.setAttribute("stepOptions", stepOptions);
        req.getRequestDispatcher("/jsp/StepEdit.jsp").forward(req, resp);
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//
//        if ("Delete".equalsIgnoreCase(action)) {
//            // Удаление опции
//            int stepId = Integer.parseInt(req.getParameter("stepId"));
//            int optionKey = Integer.parseInt(req.getParameter("optionKey"));
//            deleteOption(stepId, optionKey);
//        } else if ("Edit".equalsIgnoreCase(action)) {
//            // Изменение опции
//            int stepId = Integer.parseInt(req.getParameter("stepId"));
//            int optionKey = Integer.parseInt(req.getParameter("optionKey"));
//            String newValue = req.getParameter("newValue");
//            editOption(stepId, optionKey, newValue);
//        } else if ("Add".equalsIgnoreCase(action)) {
//            // Добавление опции
//            int stepId = Integer.parseInt(req.getParameter("stepId"));
//            int newOptionKey = Integer.parseInt(req.getParameter("newOptionKey"));
//            String newOptionValue = req.getParameter("newOptionValue");
//            addOption(stepId, newOptionKey, newOptionValue);
//        }
//
//        // После обработки действия перенаправляем обратно на страницу редактора
//        resp.sendRedirect(req.getContextPath() + "/questEdit");
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = req.getParameter("action");
        int questId = Integer.parseInt(req.getParameter("questId"));
        int stepId = Integer.parseInt(req.getParameter("stepId"));
        int optionKey = Integer.parseInt(req.getParameter("optionKey"));
        String newValue = req.getParameter("optionValue");

        switch (action) {
            case "add" -> {
                int newOptionKey = Integer.parseInt(req.getParameter("optionKey"));
                String newOptionValue = req.getParameter("optionValue");
                Step stepToAdd = QuestService.getStepById(stepId);
                stepToAdd.addOption(newOptionKey, newOptionValue);
                QuestService.updateStep(stepToAdd);
            }
            case "delete" -> {
                Step stepToDelete = QuestService.getStepById(stepId);
                stepToDelete.getOptions().remove(optionKey);
                QuestService.updateStep(stepToDelete);
            }
            case "edit" -> {
                Step step = QuestService.getStepById(stepId);
                if (step != null) {
                    step.getOptions().put(optionKey, newValue);
                    QuestService.updateStep(step);
                }
            }
        }

        // После обработки перенаправляем обратно на страницу редактирования
        resp.sendRedirect(req.getContextPath() + "/stepEdit");
    }


    // Логика удаления опции
    private void deleteOption(int stepId, int optionKey) {
        Step step = QuestService.getStepById(stepId);
        if (step != null) {
            step.getOptions().remove(optionKey);
            QuestService.updateStep(step);
        }
    }

    // Логика изменения опции
    private void editOption(int stepId, int optionKey, String newValue) {
        Step step = QuestService.getStepById(stepId);
        if (step != null) {
            step.getOptions().put(optionKey, newValue);
            QuestService.updateStep(step);
        }
    }

    // Логика добавления опции
    private void addOption(int stepId, int optionKey, String optionValue) {
        Step step = QuestService.getStepById(stepId);
        if (step != null) {
            step.addOption(optionKey, optionValue);
            QuestService.updateStep(step);
        }
    }

}
