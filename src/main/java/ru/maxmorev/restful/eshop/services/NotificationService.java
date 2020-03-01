package ru.maxmorev.restful.eshop.services;

public interface NotificationService {

    void emailVerification(String email, String name, String code);
    void orderPaymentConfirmation(String email, String name, long orderId);

}
