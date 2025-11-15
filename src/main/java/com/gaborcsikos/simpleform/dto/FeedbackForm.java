package com.gaborcsikos.simpleform.dto;

import com.gaborcsikos.simpleform.entity.ContactType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackForm {

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Contact type is required")
    private ContactType contactType;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message can be at most 1000 characters")
    private String message;

}
