package ru.maxmorev.restful.eshop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.entities.Commodity;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface OrderPurchaseService {

    Optional<CustomerOrder> findOrder(Long id);

    CustomerOrder createOrderFor(Customer customer);

    CustomerOrder confirmPaymentOrder(CustomerOrder order, PaymentProvider paymentProvider, String paymentID);

    List<CustomerOrder> findCustomerOrders(Long customerId);

    void cleanExpiredOrders();

    CustomerOrder setOrderStatus(Long id, CustomerOrderStatus status);

    List<CustomerOrder> findCustomerOrders(Long customerId, CustomerOrderStatus status);

    Page<CustomerOrder> findAllOrdersByPage(Pageable pageable);
    Page<CustomerOrder> findAllOrdersByPageAndStatus(Pageable pageable, CustomerOrderStatus status);
    Page<CustomerOrder> findAllOrdersByPageAndStatusNot(Pageable pageable, CustomerOrderStatus status);
}
