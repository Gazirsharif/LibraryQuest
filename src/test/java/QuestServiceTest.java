import com.libraryquest.dao.QuestLoader;
import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestServiceTest {
    private QuestService questService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questService = new QuestService();
    }

    @Test
    void testGetQuestById() {
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(() -> QuestLoader.getQuestById(1)).thenReturn(mockQuest);

            Quest result = QuestService.getQuestById(1);

            assertNotNull(result);
            assertEquals(1, result.getQuestId());
            assertEquals("Test Quest", result.getTitle());
        }
    }

    @Test
    void testGetAllQuests() {
        List<Quest> mockQuests = List.of(new Quest(1, "Test Quest", "Description", new ArrayList<>()));

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(QuestLoader::getAllQuests).thenReturn(mockQuests);

            List<Quest> result = QuestService.getAllQuests();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Quest", result.get(0).getTitle());
        }
    }

    @Test
    void testSaveQuest() {
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(() -> QuestLoader.saveQuest(mockQuest)).then(invocation -> {
                Quest savedQuest = invocation.getArgument(0);
                assertEquals("Test Quest", savedQuest.getTitle());
                assertEquals("Description", savedQuest.getDescription());
                return null;
            });

            QuestService.saveQuest(mockQuest);
        }
    }

    @Test
    void testDeleteQuest() {
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(() -> QuestLoader.deleteQuest(mockQuest)).then(invocation -> {
                Quest deletedQuest = invocation.getArgument(0);
                assertEquals(1, deletedQuest.getQuestId());
                return null;
            });

            QuestService.deleteQuest(mockQuest);
        }
    }

    @Test
    void testGetStepContent() {
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());
        Step mockStep = new Step(mockQuest, 1, "Test Question", null);
        mockQuest.getSteps().add(mockStep);

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(() -> QuestLoader.getStepContent(1, 1)).thenReturn(mockStep);

            Step result = QuestService.getStepContent(1, 1);

            assertNotNull(result);
            assertEquals(1, result.getStepId());
            assertEquals("Test Question", result.getQuestion());
        }
    }

    @Test
    void testUpdateStep() {
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());
        Step mockStep = new Step(mockQuest, 1, "Test Question", null);
        mockQuest.getSteps().add(mockStep);

        try (MockedStatic<QuestLoader> questLoaderMock = Mockito.mockStatic(QuestLoader.class)) {
            questLoaderMock.when(() -> QuestLoader.updateStep(mockStep)).then(invocation -> {
                Step updatedStep = invocation.getArgument(0);
                assertEquals(1, updatedStep.getStepId());
                assertEquals("Test Question", updatedStep.getQuestion());
                return null;
            });

            QuestService.updateStep(mockStep);
        }
    }
}
