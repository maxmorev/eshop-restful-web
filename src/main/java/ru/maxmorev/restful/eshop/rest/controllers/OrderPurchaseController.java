package ru.maxmorev.restful.eshop.rest.controllers;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.OrderIdRequest;
import ru.maxmorev.restful.eshop.rest.request.OrderPaymentConfirmation;
import ru.maxmorev.restful.eshop.rest.response.CustomerOrderDto;
import ru.maxmorev.restful.eshop.rest.response.Message;
import ru.maxmorev.restful.eshop.rest.response.OrderGrid;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "order/{id}/customer/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    CustomerOrderDto customerOrder(
            @PathVariable(name = "customerId") Long customerId,
            @PathVariable(name = "id") Long orderId,
            Locale locale) {
        //TODO check auth customer.id with id in PathVariable
        return orderPurchaseService
                .findOrder(orderId, customerId)
                .map(CustomerOrderDto::of)
                .orElseThrow(() -> new NoSuchElementException("No such order"));
    }

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "order/list/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    List<CustomerOrderDto> customerOrderList(@PathVariable(name = "customerId", required = true) Long customerId, Locale locale) {
        //TODO check auth customer.id with id in PathVariable
        return orderPurchaseService.findOrderListForCustomer(customerId);
    }

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "order/cancel/", method = RequestMethod.PUT)
    @ResponseBody
    Message customerOrderCancel(
            @Valid @RequestBody OrderIdRequest order,
            Locale locale) {
        //TODO check auth customer.id with id in PathVariable
        orderPurchaseService.cancelOrderByCustomer(order.getOrderId());
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }


    @RequestMapping(path = Constants.REST_PRIVATE_URI + "order/{status}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Message setOrderStatus(
            @PathVariable(name = "status", required = true) String status,
            @PathVariable(name = "id", required = true) Long id,
            Locale locale) {
        Optional<CustomerOrderStatus> orderStatus = Enums.getIfPresent(CustomerOrderStatus.class, status);
        if (!orderStatus.isPresent())
            throw new IllegalArgumentException("Invalid status");
        orderPurchaseService.setOrderStatus(id, orderStatus.get());
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = Constants.REST_PRIVATE_URI + "order/list/", method = RequestMethod.GET)
    @ResponseBody
    OrderGrid adminAllCustomerOrderList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            Locale locale) {

        return orderPurchaseService
                .getOrdersForAdmin(page,
                        rows,
                        sortBy,
                        order);
    }

}
