package com.gaborcsikos.simpleform.repository;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByContactType(ContactType contactType, Sort sort);
}
