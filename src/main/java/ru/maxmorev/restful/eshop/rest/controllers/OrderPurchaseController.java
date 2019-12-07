package ru.maxmorev.restful.eshop.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.OrderPaymentConfirmation;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import javax.validation.Valid;
import java.util.Locale;

@RestController
public class OrderPurchaseController {

    private MessageSource messageSource;
    private OrderPurchaseService orderPurchaseService;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setOrderPurchaseService(OrderPurchaseService orderPurchaseService) {
        this.orderPurchaseService = orderPurchaseService;
    }

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "order/confirm/", method = RequestMethod.POST)
    @ResponseBody
    Message confirmOrder(@RequestBody
                         @Valid OrderPaymentConfirmation orderPaymentConfirmation,
                         Locale locale) {

        orderPurchaseService.findOrder(orderPaymentConfirmation.getOrderId()).ifPresent(order -> {
            orderPurchaseService.confirmPaymentOrder(
                    order,
                    PaymentProvider.valueOf(orderPaymentConfirmation.getPaymentProvider()),
                    orderPaymentConfirmation.getPaymentId()
            );
        });

        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }


}
