package com.gaborcsikos.simpleform;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminFeedbackControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @BeforeEach
    void setUp() {
        feedbackRepository.deleteAll();

        Feedback f1 = new Feedback();
        f1.setName("User1");
        f1.setEmail("u1@example.com");
        f1.setContactType(ContactType.SUPPORT);
        f1.setMessage("Support issue 1");
        f1.setSubmittedAt(LocalDateTime.now().minusHours(1));

        Feedback f2 = new Feedback();
        f2.setName("User2");
        f2.setEmail("u2@example.com");
        f2.setContactType(ContactType.GENERAL);
        f2.setMessage("General question");
        f2.setSubmittedAt(LocalDateTime.now());

        feedbackRepository.save(f1);
        feedbackRepository.save(f2);
    }

    @Test
    void listFeedback_shouldRenderTableWithSubmissions() throws Exception {
        mockMvc.perform(get("/admin/feedback"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_list"))
                .andExpect(content().string(containsString("Feedback submissions")))
                .andExpect(content().string(containsString("Support issue 1")))
                .andExpect(content().string(containsString("General question")));
    }

    @Test
    void listFeedback_withFilterSupport_shouldOnlyContainSupportMessages() throws Exception {
        mockMvc.perform(get("/admin/feedback")
                        .param("type", "SUPPORT")
                        .param("sort", "ASC"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback_list"))
                .andExpect(content().string(containsString("Support issue 1")))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("General question"))));
    }
}
