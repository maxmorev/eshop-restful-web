package ru.maxmorev.restful.eshop.feignclient.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


public class OrderPaymentConfirmedAdminTemplate {

    public MailSendRequest create(String destination, long orderId) {
        return new MailSendRequest(
                "OrderPaymentConfirmedAdmin",
                destination,
                Map.of("order-id", String.valueOf(orderId)));
    }

}
