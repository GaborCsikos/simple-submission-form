package com.gaborcsikos.simpleform.controller;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminFeedbackController.class)
class AdminFeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @Test
    void listFeedback_withoutParams_shouldCallServiceWithoutFilterAndUseDescSort() throws Exception {
        Feedback f = new Feedback();
        f.setName("John");
        f.setContactType(ContactType.GENERAL);
        f.setMessage("Hi");
        f.setSubmittedAt(LocalDateTime.now());

        when(feedbackService.listFeedback(any(), any())).thenReturn(List.of(f));

        mockMvc.perform(get("/admin/feedback"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_list"))
                .andExpect(model().attributeExists("feedbackList", "contactTypes", "sortDirection"));

        verify(feedbackService).listFeedback(Optional.empty(), Sort.Direction.DESC);
    }

    @Test
    void listFeedback_withTypeAndSort_shouldPassParamsToService() throws Exception {
        when(feedbackService.listFeedback(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/admin/feedback")
                        .param("type", "SUPPORT")
                        .param("sort", "ASC"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_list"));

        verify(feedbackService).listFeedback(Optional.of(ContactType.SUPPORT), Sort.Direction.ASC);
    }
}
