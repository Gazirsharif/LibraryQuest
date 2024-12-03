package com.libraryquest.utils;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Класс для загрузки квестов из базы данных или файлов
 */
public class QuestLoader {

    /**
     * Получение квеста по ID
     */
    public static Quest getQuestById(int questId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Quest.class, questId);
        }
    }

    /**
     * Получение всех квестов
     */
    public static List<Quest> getAllQuests() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Quest", Quest.class).list();
        }
    }

    /**
     * Сохранение нового квеста или обновление существующего
     */
    public static void saveQuest(Quest quest) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(quest);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Удаление квеста по ID
     */
    public static void deleteQuest(int questId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Quest quest = session.get(Quest.class, questId);
            if (quest != null) {
                session.remove(quest);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Получение конкретного шага по ID квеста и ID шага
     */
    public static Step getStepContent(int questId, int stepId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Quest quest = getQuestById(questId);
            if (quest != null) {
                return quest.getSteps().stream()
                        .filter(step -> step.getStepId() == stepId)
                        .findFirst()
                        .orElse(null);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Проверяет, есть ли данные в базе, и загружает начальные данные, если нужно
     */
    public static void initializeData() {
        if (getAllQuests().isEmpty()) {
            loadDefaultQuests();
        }
    }

    /**
     * Загружает данные по умолчанию
     */
    private static void loadDefaultQuests() {
        Quest quest = new Quest();
        quest.setTitle("Красная Шапочка");
        quest.setDescription("Классический квест про девочку и волка");

        Step step1 = new Step(quest, 1, "Вы находитесь на опушке леса. Перед вами тропинка.", Map.of(
                2, "Пойти по тропинке к дому бабушки",
                4, "Сойти с тропинки и углубиться в лес"
        ));
        Step step2 = new Step(quest, 2, "Вы идете по тропинке и встречаете волка. Что вы делаете?", Map.of(
                3, "Поздороваться с волком и рассказать, куда идете",
                6, "Попытаться обойти волка незаметно"
        ));

        Step step3 = new Step(quest, 3, "Волк улыбается и спрашивает, где живет ваша бабушка. Что вы делаете?", Map.of(
                5, "Рассказать волку адрес бабушки",
                6, "Сказать, что это секрет"
        ));

        Step step4 = new Step(quest, 4, "Вы углубляетесь в лес и находите корзинку с пирожками. Что вы делаете?", Map.of(
                7, "Забрать корзинку с собой",
                8, "Оставить корзинку и вернуться на тропинку"
        ));

        // Шаги-концовки
        Step step5 = new Step(quest, 5, "Волк первым добирается к бабушке и захватывает дом! Вам нужно спасать бабушку.", null);
        Step step6 = new Step(quest, 6, "Вы не рассказываете волку, куда идете, и успеваете добраться до бабушки раньше. Вы в безопасности!", null);
        Step step7 = new Step(quest, 7, "Вы забираете корзинку. В ней оказываются волшебные пирожки, которые помогают вам быстрее дойти до бабушки.", null);
        Step step8 = new Step(quest, 8, "Вы оставляете корзинку и возвращаетесь на тропинку. Волк все равно находит вас позже.", null);

        quest.setSteps(List.of(step1, step2, step3, step4, step5, step6, step7, step8));
        saveQuest(quest);
    }
}