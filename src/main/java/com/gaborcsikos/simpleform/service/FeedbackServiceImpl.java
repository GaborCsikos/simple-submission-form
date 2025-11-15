package com.gaborcsikos.simpleform.service;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.mapper.FeedbackMapper;
import com.gaborcsikos.simpleform.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public Feedback submitFeedback(FeedbackForm form) {
        log.info("submit feedback method called with form: {}", form);
        Feedback feedback = feedbackMapper.toEntity(form);
        log.info("submit feedback method called with feedback: {}", feedback);
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> listFeedback(Optional<ContactType> contactType,
                                       Sort.Direction direction) {

        Sort sort = Sort.by(direction, "submittedAt");
        log.info("list feedback method called with contactType: {}", contactType);
        if (contactType.isPresent()) {
            log.debug("list feedback method called with contactType: {}", contactType.get());
            return feedbackRepository.findByContactType(contactType.get(), sort);
        } else {
            log.debug("list feedback method called with empty contactType");
            return feedbackRepository.findAll(sort);
        }
    }
}
