package com.gaborcsikos.simpleform.controller;

import com.gaborcsikos.simpleform.dto.FeedbackForm;
import com.gaborcsikos.simpleform.entity.ContactType;
import com.gaborcsikos.simpleform.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/")
    public String showForm(Model model,
                           @RequestParam(value = "success", required = false) String success) {
        model.addAttribute("feedbackForm", new FeedbackForm());
        model.addAttribute("contactTypes", ContactType.values());
        model.addAttribute("success", success != null);
        return "feedback_form";
    }

    @PostMapping("/submit")
    public String submitFeedback(@Valid @ModelAttribute("feedbackForm") FeedbackForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contactTypes", ContactType.values());
            return "feedback_form";
        }

        feedbackService.submitFeedback(form);
        return "redirect:/?success=true";
    }
}