package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.ShoppingCartSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderPurchaseService {

    Optional<CustomerOrder> findOrder(Long id);

    CustomerOrder createOrderFor(Customer customer);

    CustomerOrder confirmPaymentOrder(CustomerOrder order, PaymentProvider paymentProvider, String paymentID);

    List<CustomerOrder> findCustomerOrders(Customer customer);

}
