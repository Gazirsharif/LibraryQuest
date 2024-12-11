package com.libraryquest.servlets;

import com.libraryquest.models.Quest;
import com.libraryquest.models.Step;
import com.libraryquest.models.StepOption;
import com.libraryquest.services.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StepEditorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private StepEditor stepEditor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stepEditor = new StepEditor();
    }

    @Test
    public void testDoGetWithEditAction() throws Exception {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("questId")).thenReturn("1");
        when(request.getParameter("stepId")).thenReturn("2");
        when(request.getParameter("optionKey")).thenReturn("3");
        when(request.getParameter("optionValue")).thenReturn("Test Option");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        List<Quest> quests = new ArrayList<>();
        Quest mockQuest = new Quest(1, "Test Quest", "Description", new ArrayList<>());
        Step mockStep = new Step(mockQuest, 2, "Test Step", Map.of(3, "Option"));
        mockQuest.getSteps().add(mockStep);
        quests.add(mockQuest);

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(QuestService::getAllQuests).thenReturn(quests);

            stepEditor.doGet(request, response);

            verify(request).setAttribute(eq("editOption"), any(StepOption.class));
            verify(request).setAttribute(eq("stepOptions"), any());
            verify(requestDispatcher).forward(request, response);
        }
    }

    @Test
    public void testDoPostAddAction() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("stepId")).thenReturn("2");
        when(request.getParameter("optionKey")).thenReturn("3");
        when(request.getParameter("optionValue")).thenReturn("New Option");

        Step mockStep = mock(Step.class);

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getStepById(2)).thenReturn(mockStep);

            stepEditor.doPost(request, response);

            verify(mockStep).addOption(3, "New Option");
            questServiceMock.verify(() -> QuestService.updateStep(mockStep));
            verify(response).sendRedirect(anyString());
        }
    }

    @Test
    public void testDoPostDeleteAction() throws Exception {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("stepId")).thenReturn("2");
        when(request.getParameter("optionKey")).thenReturn("3");

        Step mockStep = mock(Step.class);
        Map<Integer, String> options = mock(Map.class);
        when(mockStep.getOptions()).thenReturn(options);

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getStepById(2)).thenReturn(mockStep);

            stepEditor.doPost(request, response);

            verify(options).remove(3);
            questServiceMock.verify(() -> QuestService.updateStep(mockStep));
            verify(response).sendRedirect(anyString());
        }
    }

    @Test
    public void testDoPostUpdateAction() throws Exception {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("stepId")).thenReturn("2");
        when(request.getParameter("optionKey")).thenReturn("3");
        when(request.getParameter("newOptionKey")).thenReturn("4");
        when(request.getParameter("optionValue")).thenReturn("Updated Option");

        Step mockStep = mock(Step.class);
        Map<Integer, String> options = mock(Map.class);
        when(mockStep.getOptions()).thenReturn(options);

        try (MockedStatic<QuestService> questServiceMock = Mockito.mockStatic(QuestService.class)) {
            questServiceMock.when(() -> QuestService.getStepById(2)).thenReturn(mockStep);

            stepEditor.doPost(request, response);

            verify(options).remove(3);
            verify(options).put(4, "Updated Option");
            questServiceMock.verify(() -> QuestService.updateStep(mockStep));
            verify(response).sendRedirect(anyString());
        }
    }
}
