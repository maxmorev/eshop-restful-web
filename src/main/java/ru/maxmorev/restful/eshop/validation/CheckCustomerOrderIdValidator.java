package ru.maxmorev.restful.eshop.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.repository.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.rest.request.OrderIdRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCustomerOrderIdValidator implements ConstraintValidator<CheckCustomerOrderId, OrderIdRequest> {

    private CustomerOrderRepository customerOrderRepository;

    @Autowired public void setCustomerOrderRepository(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Override
    public boolean isValid(OrderIdRequest value, ConstraintValidatorContext context) {
        return customerOrderRepository.existsById(value.getOrderId());
    }

    @Override
    public void initialize(CheckCustomerOrderId constraintAnnotation) {
        //do nothing
    }
}
