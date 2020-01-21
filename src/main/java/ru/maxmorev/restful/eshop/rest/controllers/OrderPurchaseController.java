package ru.maxmorev.restful.eshop.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.OrderPaymentConfirmation;
import ru.maxmorev.restful.eshop.rest.response.CustomerOrderDto;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "order/list/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    List<CustomerOrderDto> customerOrderList(@PathVariable(name = "customerId", required = true) Long customerId, Locale locale) {
        return orderPurchaseService
                .findCustomerOrders(customerId)
                .stream()
                .sorted()
                .map(CustomerOrderDto::of)
                .collect(Collectors.toList());
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI + "order/{status}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Message prepareToShip(
            @PathVariable(name = "status", required = true) String status,
            @PathVariable(name = "id", required = true) Long id,
            Locale locale) {
        CustomerOrderStatus orderStatus = CustomerOrderStatus.valueOf(status);
        orderPurchaseService.setOrderStatus(id, orderStatus);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

}
