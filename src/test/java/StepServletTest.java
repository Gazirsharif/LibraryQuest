import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import com.libraryquest.servlets.StepServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

class StepServletTest {

    private StepServlet stepServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        stepServlet = new StepServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void testDoGetWithValidQuestAndStepIds() throws ServletException, IOException {
        try (MockedStatic<QuestService> questServiceMockedStatic = mockStatic(QuestService.class)) {
            // Mocking Step and Quest
            Quest quest = new Quest("Test Quest", "Description");
            Step step = new Step(quest, "First Step");
            quest.setSteps(new ArrayList<>() {{
                add(step);
            }});

            when(request.getPathInfo()).thenReturn("/1/1");
            questServiceMockedStatic.when(() -> QuestService.getStepContent(1, 1)).thenReturn(step);
            when(request.getRequestDispatcher("/jsp/step.jsp")).thenReturn(requestDispatcher);

            // Invoke the servlet
            stepServlet.doGet(request, response);

            // Verify
            verify(request).setAttribute("quest", quest);
            verify(request).setAttribute("step", step);
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    void testDoGetWithInvalidPathInfo() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/");

        stepServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный маршрут для шагов");
    }

    @Test
    void testDoGetWithNonNumericIds() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc/def");

        stepServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Идентификаторы должны быть числовыми");
    }

    @Test
    void testDoGetWithStepNotFound() throws ServletException, IOException {
        try (MockedStatic<QuestService> questServiceMockedStatic = mockStatic(QuestService.class)) {
            when(request.getPathInfo()).thenReturn("/1/99");
            questServiceMockedStatic.when(() -> QuestService.getStepContent(1, 99)).thenReturn(null);

            stepServlet.doGet(request, response);

            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Шаг не найден");
        }
    }

    @Test
    void testDoGetWithInvalidPathParts() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1"); // Missing stepId

        stepServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный маршрут для шагов, путь должен быть вида questId/stepId");
    }
}
