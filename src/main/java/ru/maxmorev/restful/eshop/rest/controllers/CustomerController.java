package ru.maxmorev.restful.eshop.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.CustomerVerify;
import ru.maxmorev.restful.eshop.rest.response.CustomerDTO;
import ru.maxmorev.restful.eshop.services.CustomerService;

import javax.validation.Valid;
import java.util.Locale;

@Slf4j
@RestController
@Transactional
public class CustomerController {

    private CustomerService customerService;
    private MessageSource messageSource;

    @Autowired public void setCustomerService(CustomerService customerService) { this.customerService = customerService; }
    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getAuthenticationCustomerId(){
        String id = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null)
            if (authentication.getPrincipal() instanceof UserDetails)
                id = ((UserDetails) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof String)
                id = (String) authentication.getPrincipal();
        return id;
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "customer/", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public CustomerDTO createCustomer(@RequestBody @Valid Customer customer, Locale locale){
        log.info("Customer : {}", customer);
        return CustomerDTO.of(customerService.createCustomerAndVerifyByEmail(customer));
    }

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "update/", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public CustomerDTO updateCustomer(@RequestBody @Valid CustomerInfo customer, Locale locale){
        log.info("Customer update : {}", customer);
        String id = getAuthenticationCustomerId();
        log.info("Authentication id = {}", id);
        /* user can update only self data */
        if(!id.equals(customer.getEmail()))
            throw new BadCredentialsException("Not Authenticated");
        Customer findByEmail = customerService.updateInfo(customer);
        return CustomerDTO.of(findByEmail);
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "customer/verify/", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public CustomerVerify verifyCustomer(@RequestBody @Valid CustomerVerify customerVerify, Locale locale){
        log.info("CustomerVerify : {}", customerVerify);
        Customer customer = customerService
                .verify(customerVerify.getId(), customerVerify.getVerifyCode())
                .orElseThrow(()-> new UsernameNotFoundException(messageSource.getMessage("customer.error.notFound", new Object[]{customerVerify.getId()}, locale)));
        customerVerify.setVerified(customer.getVerified());
        return customerVerify;
    }


}
