package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AuthorityValues;
import ru.maxmorev.restful.eshop.domain.Mail;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.repos.CustomerRepository;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private CustomerRepository customerRepository;
    private PasswordEncoder bcryptEncoder;
    private MessageSource messageSource;
    private MailService mailService;
    private ShoppingCartService shoppingCartService;

    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @Autowired public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }
    @Autowired public void setMailService(MailService ms){ this.mailService = ms; }
    @Autowired public void setShoppingCartService(ShoppingCartService shoppingCartService) { this.shoppingCartService = shoppingCartService; }
    @Autowired public void setCustomerRepository(CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

    @Override
    public Customer createCustomerAndVerifyByEmail(Customer customer) {
        Customer created = null;

        Customer findByEmail = findByEmail(customer.getEmail())
                .orElseThrow( ()->new IllegalArgumentException(
                                messageSource.getMessage("customer.error.unique.email",
                                    new Object[]{customer.getEmail()},
                                    LocaleContextHolder.getLocale() )
                ) );

        Mail mail = Mail
                .builder()
                .to(created.getEmail())
                .subject(messageSource.getMessage("mail.plain.subject", new Object[]{}, LocaleContextHolder.getLocale()))
                .text(messageSource.getMessage("mail.plain.text", new Object[]{created.getFullName(), created.getVerifyCode()}, LocaleContextHolder.getLocale()))
                .build();
        Boolean isMailSend = mailService.sendPlainEmail(mail);
        if(isMailSend){
            customer.setVerifyCode(RandomStringUtils.randomAlphabetic(5));
            customer.setPassword(bcryptEncoder.encode(customer.getPassword()));
            customer.removeAllAuthorities();
            customer.addAuthority(AuthorityValues.CUSTOMER);
            ShoppingCart sc = null;
            if(Objects.nonNull(customer.getShoppingCartId())) {
                sc = shoppingCartService.findShoppingCartById(customer.getShoppingCartId());
                customer.setShoppingCart(sc);
            }

            created = customerRepository.save(customer);
            if(sc!=null) {
                sc.setCustomer(created);
                shoppingCartService.update(sc);
            }
        }else{
            throw new RuntimeException( messageSource.getMessage("mail.sending.error", new Object[]{created.getFullName(), created.getVerifyCode()}, LocaleContextHolder.getLocale()) );
        }

        return created;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void update(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> verify(Long customerId, String code) {
        Optional<Customer> oC = customerRepository.findById(customerId);
        log.info("......oC.isPresent()>" + oC.isPresent());
        oC.ifPresent(customer->{
            log.info("......oC.get().getVerifyCode()>" + oC.get().getVerifyCode());
            if(customer.getVerifyCode().equals(code)){
                customer.setVerified(true);
                customerRepository.save(customer);
            }
        });
        return oC;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> find(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username)
                .orElseThrow( ()-> new UsernameNotFoundException(messageSource.getMessage("customer.error.notFound",
                        new Object[]{username}, LocaleContextHolder.getLocale() )) );
    }

    @Override
    public Customer updateInfo(CustomerInfo i) {
        Customer findByEmail = findByEmail(i.getEmail())
                .orElseThrow(()->new IllegalArgumentException(
                        messageSource.getMessage("customer.error.notFound",
                                new Object[]{i.getEmail()}, LocaleContextHolder.getLocale()))
                );

        findByEmail.setAddress(i.getAddress());
        findByEmail.setCity(i.getCity());
        findByEmail.setPostcode(i.getPostcode());
        findByEmail.setCountry(i.getCountry());
        findByEmail.setFullName(i.getFullName());
        return customerRepository.save(findByEmail);
    }
}
