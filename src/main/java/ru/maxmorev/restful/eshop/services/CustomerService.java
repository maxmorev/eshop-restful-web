package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;

import java.util.Optional;

public interface CustomerService {

    Customer createCustomerAndVerifyByEmail(Customer customer);

    void update(Customer customer);

    Customer updateInfo(CustomerInfo i);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> verify(Long customerId, String code);

}
