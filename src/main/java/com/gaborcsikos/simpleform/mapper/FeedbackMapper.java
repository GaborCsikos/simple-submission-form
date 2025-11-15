package com.gaborcsikos.simpleform.mapper;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback toEntity(FeedbackForm form) {
        if (form == null) {
            return null;
        }
        Feedback feedback = new Feedback();
        feedback.setName(form.getName());
        feedback.setEmail(form.getEmail());
        feedback.setContactType(form.getContactType());
        feedback.setMessage(form.getMessage());
        return feedback;
    }
}
