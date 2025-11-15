package com.gaborcsikos.simpleform.controller;

import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.entity.Feedback;
import com.gaborcsikos.simpleform.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public String listFeedback(
            @RequestParam(value = "type", required = false) ContactType type,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort,
            Model model) {

        Sort.Direction direction = sort.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        List<Feedback> feedbackList = feedbackService.listFeedback(
                Optional.ofNullable(type),
                direction
        );

        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("contactTypes", ContactType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("sortDirection", direction.name());

        return "feedback_list";
    }
}