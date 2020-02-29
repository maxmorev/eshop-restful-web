package ru.maxmorev.restful.eshop.feignclient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MailSendRequest {
    private String template;
    private String destination;
    private Map<String, String> templateData = new HashMap<>();

}
