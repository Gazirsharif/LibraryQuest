package com.libraryquest.services;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.utils.QuestLoader;

import java.util.List;

/**
 * Класс, отвечающий за загрузку и хранение квестов, управление шагами и переходами.
 */
public class QuestService {
    public static Quest getQuestById(int questId) {
        Quest questById = QuestLoader.getQuestById(questId);
        if (questById == null) {
            QuestLoader.initializeData();
        }
        return QuestLoader.getQuestById(questId);
    }

    public static Step getStepContent(int questId, int stepId) {
        return QuestLoader.getStepContent(questId, stepId);
    }

    public static List<Quest> getAllQuests() {
        return QuestLoader.getAllQuests();
    }

    public static void deleteQuest(int questId) {
        QuestLoader.deleteQuest(questId);
    }

    public static void saveQuest(Quest updatedQuest) {
        QuestLoader.saveQuest(updatedQuest);
    }

    public static void updateStep(Step step) {
        QuestLoader.updateStep(step);
    }

    public static void deleteStepById(int questId, int stepId) {
        QuestLoader.deleteStepById(questId, stepId);
    }

    public static Step getStepById(int stepId) {
        // Реализуйте получение шага из базы данных
        return QuestLoader.getStepById(stepId);
    }

    // Дополнительные методы логики
    public void markQuestAsCompleted(int questId) {
        // Логика завершения квеста
    }

    public void resetQuestProgress(int questId) {
        // Логика сброса прогресса
    }
}
