package com.gaborcsikos.simpleform;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    void getForm_shouldReturnOkAndContainForm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_form"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Send us your feedback")));
    }

    @Test
    void submitFeedback_valid_shouldPersistAndRedirect() throws Exception {
        feedbackRepository.deleteAll();

        mockMvc.perform(post("/submit")
                        .param("name", "Jane Doe")
                        .param("email", "jane@example.com")
                        .param("contactType", "MARKETING")
                        .param("message", "Nice site!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?success=true"));

        List<Feedback> all = feedbackRepository.findAll();
        assertThat(all).hasSize(1);

        Feedback saved = all.getFirst();
        assertThat(saved.getName()).isEqualTo("Jane Doe");
        assertThat(saved.getEmail()).isEqualTo("jane@example.com");
        assertThat(saved.getContactType()).isEqualTo(ContactType.MARKETING);
        assertThat(saved.getMessage()).isEqualTo("Nice site!");
        assertThat(saved.getSubmittedAt()).isNotNull();
    }
}
