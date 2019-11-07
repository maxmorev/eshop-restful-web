package ru.maxmorev.restful.eshop.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Mail {
    private String to;
    private String subject;
    private String text;

}
