package ru.maxmorev.restful.eshop.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxmorev.restful.eshop.domain.Mail;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.CustomerVerify;
import ru.maxmorev.restful.eshop.rest.response.CustomerDTO;
import ru.maxmorev.restful.eshop.services.CustomerService;
import ru.maxmorev.restful.eshop.services.FutureRunner;
import ru.maxmorev.restful.eshop.services.MailService;
import ru.maxmorev.restful.eshop.services.ShoppingCartService;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;

@RestController
@Transactional
public class CustomerController {

    private final static Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    private ShoppingCartService shoppingCartService;
    private CustomerService customerService;
    private MessageSource messageSource;
    private FutureRunner futureRunner;
    private MailService mailService;


    @Autowired public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired public void setFutureRunner(FutureRunner fr){ this.futureRunner = fr; }

    @Autowired public void setMailService(MailService ms){ this.mailService = ms; }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "customer/", method = RequestMethod.POST)
    @ResponseBody
    public Customer createCustomer(@RequestBody @Valid Customer customer, Locale locale){
        logger.info("Customer : " + customer);

        Customer created = null;

        Customer findByEmail = customerService.findByEmail(customer.getEmail());
        if( Objects.nonNull(findByEmail) ){
            throw new IllegalArgumentException(messageSource.getMessage("customer.error.unique.email", new Object[]{customer.getEmail()}, locale));
        }

        created = customerService.createCustomer(customer);
        Mail mail = new Mail()
                    .to(created.getEmail())
                    .subject(messageSource.getMessage("mail.plain.subject", new Object[]{}, locale))
                    .text(messageSource.getMessage("mail.plain.text", new Object[]{created.getFullName(), created.getVerifyCode()}, locale));
        Boolean isMailSend = mailService.sendPlainEmail(mail);
        if(Objects.nonNull(customer.getShoppingCartId())) {
            ShoppingCart sc = shoppingCartService.findShoppingCartById(customer.getShoppingCartId());
            created.setShoppingCart(sc);
            customerService.update(created);
            sc.setCustomer(created);
            shoppingCartService.update(sc);
        }
        return created;
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

    @RequestMapping(path = Constants.REST_CUSTOMER_URI + "update/", method = RequestMethod.PUT)
    @ResponseBody
    public CustomerDTO updateCustomer(@RequestBody @Valid CustomerInfo customer, Locale locale){
        logger.info("Customer update : " + customer);
        String id = getAuthenticationCustomerId();
        if(id==null)
            throw new IllegalAccessError("Not Authenticated");

        Customer findByEmail = customerService.findByEmail(id);
        if( Objects.isNull(findByEmail) ){
            throw new IllegalArgumentException(messageSource.getMessage("customer.error.notFound", new Object[]{id}, locale));
        }

        findByEmail.setAddress(customer.getAddress());
        findByEmail.setCity(customer.getCity());
        findByEmail.setPostcode(customer.getPostcode());
        findByEmail.setCountry(customer.getCountry());
        findByEmail.setFullName(customer.getFullName());
        customerService.update(findByEmail);
        return CustomerDTO.build(findByEmail);
    }

    @RequestMapping(path = Constants.REST_PUBLIC_URI + "customer/verify/", method = RequestMethod.POST)
    @ResponseBody
    public CustomerVerify verifyCustomer(@RequestBody @Valid CustomerVerify customerVerify, Locale locale){
        logger.info("CustomerVerify : " + customerVerify);
        Customer customer = customerService.verify(customerVerify.getId(), customerVerify.getVerifyCode());
        if(Objects.nonNull(customer)){
            customerVerify.setVerified(customer.getVerified());
        }else{
            throw new UsernameNotFoundException(messageSource.getMessage("customer.error.notFound", new Object[]{customerVerify.getId()}, locale));
        }

        return customerVerify;
    }


}
