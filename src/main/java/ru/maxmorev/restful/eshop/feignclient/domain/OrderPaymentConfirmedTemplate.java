package ru.maxmorev.restful.eshop.feignclient.domain;

import java.util.Map;

public class OrderPaymentConfirmedTemplate {

    public MailSendRequest create(String destination, String name, long orderId) {
        return new MailSendRequest(
                "OrderPaymentConfirmed",
                destination,
                Map.of("name", name, "order-id", String.valueOf(orderId), "site", "titsonfire.store"));
    }

}
