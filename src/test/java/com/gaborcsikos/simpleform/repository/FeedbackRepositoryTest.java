package com.gaborcsikos.simpleform.repository;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    void findByContactType_shouldReturnOnlyMatchingType_sortedBySubmittedAt()  {
        Feedback general = new Feedback();
        general.setName("Gen");
        general.setContactType(ContactType.GENERAL);
        general.setMessage("General message");

        Feedback support1 = new Feedback();
        support1.setName("Sup1");
        support1.setContactType(ContactType.SUPPORT);
        support1.setMessage("Support 1");

        feedbackRepository.save(general);
        feedbackRepository.save(support1);

        List<Feedback> result = feedbackRepository.findByContactType(
                ContactType.SUPPORT,
                Sort.by(Sort.Direction.DESC, "submittedAt")
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getContactType()).isEqualTo(ContactType.SUPPORT);
        assertThat(result.getFirst().getMessage()).isEqualTo("Support 1");
    }
}
