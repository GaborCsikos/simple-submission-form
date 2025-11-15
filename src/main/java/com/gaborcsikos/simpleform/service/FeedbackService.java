package com.gaborcsikos.simpleform.service;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    Feedback submitFeedback(FeedbackForm form);

    List<Feedback> listFeedback(Optional<ContactType> contactType,
                                Sort.Direction direction);
}
