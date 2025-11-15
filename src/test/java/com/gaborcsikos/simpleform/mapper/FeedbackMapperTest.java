package com.gaborcsikos.simpleform.mapper;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackMapperTest {

    private final FeedbackMapper mapper = new FeedbackMapper();

    @Test
    void toEntity_shouldReturnNull_whenFormIsNull() {
        Feedback result = mapper.toEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        FeedbackForm form = new FeedbackForm();
        form.setName("John Doe");
        form.setEmail("john@example.com");
        form.setContactType(ContactType.SUPPORT);
        form.setMessage("Help me!");

        Feedback result = mapper.toEntity(form);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getContactType()).isEqualTo(ContactType.SUPPORT);
        assertThat(result.getMessage()).isEqualTo("Help me!");
    }

    @Test
    void toEntity_shouldAllowNullOptionalFields() {
        FeedbackForm form = new FeedbackForm();
        form.setName(null);
        form.setEmail(null);
        form.setContactType(ContactType.GENERAL);
        form.setMessage("No email and no name.");

        Feedback result = mapper.toEntity(form);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getEmail()).isNull();
        assertThat(result.getContactType()).isEqualTo(ContactType.GENERAL);
        assertThat(result.getMessage()).isEqualTo("No email and no name.");
    }
}
