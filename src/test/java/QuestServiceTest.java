import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import com.libraryquest.dao.QuestLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestServiceTest {

    private QuestLoader questLoaderMock;

    @BeforeEach
    void setUp() {
        questLoaderMock = mock(QuestLoader.class);
    }

    @Disabled
    @Test
    void testGetQuestById() {
        Quest quest = new Quest();
        quest.setQuestId(1);
        quest.setTitle("Test Quest");

        when(questLoaderMock.getQuestById(1)).thenReturn(quest);

        Quest result = QuestService.getQuestById(1);
        assertNotNull(result);
        assertEquals(1, result.getQuestId());
        assertEquals("Test Quest", result.getTitle());
    }

    @Disabled
    @Test
    void testGetStepContent() {
        Quest quest = new Quest();
        Step step = new Step();
        step.setStepId(1);
        step.setQuestion("Test Step");
        quest.setSteps(List.of(step));

        when(questLoaderMock.getStepContent(1, 1)).thenReturn(step);

        Step result = QuestService.getStepContent(1, 1);
        assertNotNull(result);
        assertEquals("Test Step", result.getQuestion());
    }
}
