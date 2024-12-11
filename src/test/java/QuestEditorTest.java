import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.services.QuestService;
import com.libraryquest.servlets.QuestEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Mockito.*;

public class QuestEditorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private QuestEditor questEditor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        questEditor = new QuestEditor();
    }

    @Test
    public void testDoGetEditAction() throws Exception {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getQuestById(1)).thenReturn(mockQuest);

            questEditor.doGet(request, response);

            verify(request).setAttribute("quest", mockQuest);
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    public void testDoPostAddAction() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("title")).thenReturn("New Quest");
        when(request.getParameter("description")).thenReturn("New Description");

        Quest newQuest = new Quest();

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.saveQuest(any(Quest.class))).then(invocation -> {
                Quest savedQuest = invocation.getArgument(0);
                newQuest.setTitle(savedQuest.getTitle());
                newQuest.setDescription(savedQuest.getDescription());
                return null;
            });

            questEditor.doPost(request, response);

            verify(response).sendRedirect(anyString());
            assert "New Quest".equals(newQuest.getTitle());
            assert "New Description".equals(newQuest.getDescription());
        }
    }

    @Test
    public void testDoPostEditAction() throws Exception {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("Updated Quest");
        when(request.getParameter("description")).thenReturn("Updated Description");

        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getQuestById(1)).thenReturn(mockQuest);
            questServiceMock.when(() -> QuestService.saveQuest(mockQuest)).then(invocation -> {
                // Update quest attributes to simulate save
                mockQuest.setTitle(request.getParameter("title"));
                mockQuest.setDescription(request.getParameter("description"));
                return null;
            });

            questEditor.doPost(request, response);

            verify(response).sendRedirect(anyString());
            assert "Updated Quest".equals(mockQuest.getTitle());
            assert "Updated Description".equals(mockQuest.getDescription());
        }
    }


//    @Disabled
    @Test
    public void testDoGetDeleteAction() throws Exception {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getQuestById(1)).thenReturn(mockQuest);

            questEditor.doGet(request, response);

            questServiceMock.verify(() -> QuestService.deleteQuest(mockQuest));
            verify(response).sendRedirect(anyString());
        }
    }
}
