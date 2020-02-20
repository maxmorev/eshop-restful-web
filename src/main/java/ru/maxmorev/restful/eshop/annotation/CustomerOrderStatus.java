package ru.maxmorev.restful.eshop.annotation;

public enum CustomerOrderStatus {

    AWAITING_PAYMENT,
    PAYMENT_APPROVED,
    PREPARING_TO_SHIP,
    DISPATCHED,
    DELIVERED,
    CANCELED_BY_CUSTOMER,
    CANCELED_BY_ADMIN

}
