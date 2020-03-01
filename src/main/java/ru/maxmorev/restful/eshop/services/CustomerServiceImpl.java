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
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;
import ru.maxmorev.restful.eshop.repository.CustomerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private CustomerRepository customerRepository;
    private PasswordEncoder bcryptEncoder;
    private MessageSource messageSource;
    private ShoppingCartService shoppingCartService;
    private NotificationService notificationService;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    protected void checkEmail(Customer customer) {
        findByEmail(customer.getEmail())
                .ifPresent(c -> new IllegalArgumentException(
                        messageSource.getMessage("customer.error.unique.email",
                                new Object[]{c.getEmail()},
                                LocaleContextHolder.getLocale())
                ));
    }

    @Override
    public Customer createCustomerAndVerifyByEmail(Customer customer) {
        Customer created = null;
        checkEmail(customer);
        customer.setVerifyCode(RandomStringUtils.randomAlphabetic(5));
        customer.setPassword(bcryptEncoder.encode(customer.getPassword()));
        customer.removeAllAuthorities();
        customer.addAuthority(AuthorityValues.CUSTOMER);
        ShoppingCart sc = null;
        if (Objects.nonNull(customer.getShoppingCartId())) {
            sc = shoppingCartService.findShoppingCartById(customer.getShoppingCartId()).get();
            customer.setShoppingCart(sc);
        }
        created = customerRepository.save(customer);
        em.flush();
        notificationService.emailVerification(
                customer.getEmail(),
                customer.getFullName(),
                customer.getVerifyCode());
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
        Optional<Customer> c = customerRepository.findById(customerId);
        c.ifPresent(customer -> {
            if (code.equals(customer.getVerifyCode())) {
                customer.setVerified(true);
                customerRepository.save(customer);
            }
        });
        return c;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("customer.error.notFound",
                        new Object[]{username}, LocaleContextHolder.getLocale())));
    }

    @Override
    public Customer updateInfo(CustomerInfo i) {
        Customer findByEmail = findByEmail(i.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(
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
