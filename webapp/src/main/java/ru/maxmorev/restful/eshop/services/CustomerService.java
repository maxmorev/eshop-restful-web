package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    void update(Customer customer);

    Customer find(Long id);

    Customer findByEmail(String email);

    Customer verify(Long customerId, String code);

}
