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
}
