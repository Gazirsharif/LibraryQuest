import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import com.libraryquest.servlets.QuestServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class QuestServletTest {

    private QuestServlet questServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        questServlet = new QuestServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void testDoGetWithValidQuestId() throws ServletException, IOException {
        try (MockedStatic<QuestService> questServiceMockedStatic = mockStatic(QuestService.class)) {
            // Mocking QuestService and Quest
            Quest quest = new Quest("Test Quest", "Description");
            Step step = new Step(quest, "First Step");

            List<Step> steps = new ArrayList<>();
            steps.add(step);
            quest.setSteps(steps);

            when(request.getPathInfo()).thenReturn("/1");
            questServiceMockedStatic.when(() -> QuestService.getQuestById(1)).thenReturn(quest);
            when(request.getRequestDispatcher("/jsp/quest.jsp")).thenReturn(requestDispatcher);

            // Invoke the servlet
            questServlet.doGet(request, response);

            // Verify
            verify(request).setAttribute("quest", quest);
            verify(request).setAttribute("firstStep", step);
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    void testDoGetWithInvalidQuestId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");
        questServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Идентификаторы должны быть числовыми");
    }

    @Test
    void testDoGetWithNoSteps() {
        try (MockedStatic<QuestService> questServiceMockedStatic = mockStatic(QuestService.class)) {
            Quest quest = new Quest("Test Quest", "Description");

            when(request.getPathInfo()).thenReturn("/1");
            questServiceMockedStatic.when(() -> QuestService.getQuestById(1)).thenReturn(quest);

            assertThrows(IllegalStateException.class, () -> questServlet.doGet(request, response));
        }
    }

    @Test
    void testDoGetWithNoQuestId() throws ServletException, IOException {
        try (MockedStatic<QuestService> questServiceMockedStatic = mockStatic(QuestService.class)) {
            List<Quest> quests = List.of(new Quest("Test Quest", "Description"));
            questServiceMockedStatic.when(QuestService::getAllQuests).thenReturn(quests);

            when(request.getPathInfo()).thenReturn(null);
            when(request.getRequestDispatcher("/jsp/index.jsp")).thenReturn(requestDispatcher);

            questServlet.doGet(request, response);

            verify(request).setAttribute("quests", quests);
            verify(requestDispatcher).forward(request, response);
        }
    }
}
