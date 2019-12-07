package ru.maxmorev.restful.eshop.domain;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class Mail {
    @NotBlank(message = "{validation.mail.to.blank}")
    private String to;
    @NotBlank(message = "{validation.mail.subject.blank}")
    private String subject;
    @NotBlank(message = "{validation.mail.text.blank}")
    private String text;

}
