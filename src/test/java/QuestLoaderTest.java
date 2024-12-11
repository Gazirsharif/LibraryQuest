import com.libraryquest.models.Quest;
import com.libraryquest.dao.HibernateUtil;
import com.libraryquest.dao.QuestLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestLoaderTest {

    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Disabled
    @Test
    void testSaveQuest() {
        Quest quest = new Quest();
        quest.setTitle("Test Quest");
        quest.setDescription("This is test quest");

        QuestLoader.saveQuest(quest);

        try (Session session = sessionFactory.openSession()) {
            List<Quest> quests = session.createQuery("FROM Quest", Quest.class).list();
            assertFalse(quests.isEmpty());
            assertEquals("Test Quest", quests.get(0).getTitle());

            QuestLoader.deleteQuest(quest);
        }
    }
}
