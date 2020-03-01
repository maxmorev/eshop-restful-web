package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.feignclient.domain.MailSendResponse;

public interface NotificationService {

    MailSendResponse emailVerification(String email, String name, String code);
    MailSendResponse orderPaymentConfirmation(String email, String name, long orderId);

}
