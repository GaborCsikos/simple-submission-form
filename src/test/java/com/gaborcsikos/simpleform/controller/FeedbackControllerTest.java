package com.gaborcsikos.simpleform.controller;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackController.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @Test
    void showForm_shouldRenderFeedbackFormTemplate() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_form"))
                .andExpect(model().attributeExists("feedbackForm"))
                .andExpect(model().attributeExists("contactTypes"));
    }

    @Test
    void submitFeedback_withValidationErrors_shouldReturnFormView() throws Exception {
        // Missing mandatory fields -> message & contactType -> validation errors
        mockMvc.perform(post("/submit")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_form"))
                .andExpect(model().attributeHasFieldErrors("feedbackForm", "contactType", "message"));

        verifyNoInteractions(feedbackService);
    }

    @Test
    void submitFeedback_validData_shouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/submit")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("contactType", "SUPPORT")
                        .param("message", "Help!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?success=true"));

        ArgumentCaptor<FeedbackForm> captor = ArgumentCaptor.forClass(FeedbackForm.class);
        verify(feedbackService).submitFeedback(captor.capture());

        FeedbackForm form = captor.getValue();
        assertThat(form.getName()).isEqualTo("John Doe");
        assertThat(form.getEmail()).isEqualTo("john@example.com");
        assertThat(form.getContactType()).isEqualTo(ContactType.SUPPORT);
        assertThat(form.getMessage()).isEqualTo("Help!");
    }
}
