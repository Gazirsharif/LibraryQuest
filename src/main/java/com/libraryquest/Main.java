package com.libraryquest;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import com.libraryquest.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        QuestLoader.initializeData();
//        Quest questById = QuestLoader.getQuestById(1);
//

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

//            Quest quest = new Quest();
//            quest.setQuestId(10);
//            quest.setTitle("Пример квеста 2");
//            quest.setDescription("Второй квест");
//
//            Step step = new Step();
//            step.setQuest(quest);
//            step.setQuestion("Вы видите тропинку. Что делаете?");
//            step.setOptions(Map.of(1, "Идти вперед", 2, "Вернуться назад"));
//
//            quest.setSteps(List.of(step));
//
//            QuestService.saveQuest(quest);

//            QuestService.deleteQuest(9);
//
//            List<Quest> allQuests = QuestService.getAllQuests();
//            for (Quest allQuest : allQuests) {
//                System.out.println(allQuest.getQuestId() + " " + allQuest.getDescription());
//            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
