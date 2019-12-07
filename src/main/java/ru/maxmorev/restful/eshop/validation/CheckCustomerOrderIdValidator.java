package ru.maxmorev.restful.eshop.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.restful.eshop.repos.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.rest.request.OrderPaymentConfirmation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckCustomerOrderIdValidator implements ConstraintValidator<CheckCustomerOrderId, OrderPaymentConfirmation> {

    private CustomerOrderRepository customerOrderRepository;

    @Autowired public void setCustomerOrderRepository(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Override
    public boolean isValid(OrderPaymentConfirmation value, ConstraintValidatorContext context) {
        return customerOrderRepository.existsById(value.getOrderId());
    }

    @Override
    public void initialize(CheckCustomerOrderId constraintAnnotation) {
        //do nothing
    }
}
