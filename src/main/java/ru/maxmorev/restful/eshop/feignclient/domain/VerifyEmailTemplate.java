package ru.maxmorev.restful.eshop.feignclient.domain;

import java.util.Map;

public class VerifyEmailTemplate {

    public MailSendRequest create(String destination, String code, String name){
        MailSendRequest mailTemplate =
                new MailSendRequest("VerifyEmail",
                destination,
                Map.of("name", name, "code", code, "site","titsonfire.store"));
        return mailTemplate;
    }

}
