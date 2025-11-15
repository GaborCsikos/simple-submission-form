package com.gaborcsikos.simpleform.service;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.mapper.FeedbackMapper;
import com.gaborcsikos.simpleform.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @Test
    void submitFeedback_shouldSaveFeedbackWithCorrectFields() {
        FeedbackForm form = new FeedbackForm();
        form.setName("John Doe");
        form.setEmail("john@example.com");
        form.setContactType(ContactType.SUPPORT);
        form.setMessage("Help me!");

        Feedback saved = new Feedback();
        saved.setName(form.getName());
        saved.setEmail(form.getEmail());
        saved.setContactType(form.getContactType());
        saved.setMessage(form.getMessage());

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(saved);
        when(feedbackMapper.toEntity(form)).thenReturn(saved);

        Feedback result = feedbackService.submitFeedback(form);

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(captor.capture());

        Feedback toSave = captor.getValue();
        assertThat(toSave.getName()).isEqualTo("John Doe");
        assertThat(toSave.getEmail()).isEqualTo("john@example.com");
        assertThat(toSave.getContactType()).isEqualTo(ContactType.SUPPORT);
        assertThat(toSave.getMessage()).isEqualTo("Help me!");

        assertThat(result).isSameAs(saved);
    }

    @Test
    void listFeedback_withoutFilter_shouldCallFindAllSorted() {
        Feedback f1 = new Feedback();
        f1.setMessage("m1");
        Feedback f2 = new Feedback();
        f2.setMessage("m2");

        when(feedbackRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(f1, f2));

        List<Feedback> result = feedbackService.listFeedback(
                Optional.empty(), Sort.Direction.DESC);

        assertThat(result).containsExactly(f1, f2);

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        verify(feedbackRepository).findAll(captor.capture());
        Sort sortUsed = captor.getValue();

        assertThat(sortUsed.getOrderFor("submittedAt"))
                .isNotNull()
                .extracting(Sort.Order::getDirection)
                .isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void listFeedback_withFilter_shouldCallFindByContactTypeSorted() {
        Feedback f1 = new Feedback();
        f1.setMessage("support1");
        f1.setContactType(ContactType.SUPPORT);
        when(feedbackRepository.findByContactType(eq(ContactType.SUPPORT), any(Sort.class)))
                .thenReturn(List.of(f1));

        List<Feedback> result = feedbackService.listFeedback(
                Optional.of(ContactType.SUPPORT), Sort.Direction.ASC);

        assertThat(result).containsExactly(f1);

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        verify(feedbackRepository).findByContactType(eq(ContactType.SUPPORT), captor.capture());
        Sort sortUsed = captor.getValue();

        assertThat(sortUsed.getOrderFor("submittedAt"))
                .isNotNull()
                .extracting(Sort.Order::getDirection)
                .isEqualTo(Sort.Direction.ASC);
    }
}
