package com.libraryquest.services;

import com.libraryquest.dao.QuestLoader;
import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.models.User;

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

    public static void deleteQuest(Quest quest) {
        QuestLoader.deleteQuest(quest);
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
        return QuestLoader.getStepById(stepId);
    }

    public static User findUserByUsername(String username) {
        return QuestLoader.findUserByUsername(username);
    }

    public static void saveUser(User user) {
        QuestLoader.saveUser(user);
    }


    // Дополнительные методы логики
    public void markQuestAsCompleted(int questId, int userId) {
        // Логика завершения квеста
    }

    public void resetQuestProgress(int questId, int userId) {
        // Логика сброса прогресса
    }
}
