package ru.maxmorev.restful.eshop.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AuthorityValues;
import ru.maxmorev.restful.eshop.entities.CustomerAuthority;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.repos.CustomerRepository;

import java.util.Objects;
import java.util.Optional;


@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private CustomerRepository customerRepository;
    private PasswordEncoder bcryptEncoder;
    private MessageSource messageSource;

    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @Autowired public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setVerifyCode(RandomStringUtils.randomAlphabetic(5));
        customer.setPassword(bcryptEncoder.encode(customer.getPassword()));
        customer.removeAllAuthorities();
        customer.addAuthority(AuthorityValues.CUSTOMER);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByEmail(String email) {
        Optional<Customer> oC = customerRepository.findByEmail(email);
        if(oC.isPresent())
            return oC.get();
        return null;
    }

    @Override
    public void update(Customer customer) {
        this.customerRepository.save(customer);
    }

    @Override
    public Customer verify(Long customerId, String code) {
        Optional<Customer> oC = customerRepository.findById(customerId);
        logger.info("......oC.isPresent()>" + oC.isPresent());
        if(oC.isPresent()){
            logger.info("......oC.get().getVerifyCode()>" + oC.get().getVerifyCode());
            if(oC.get().getVerifyCode().equals(code)){
                Customer customer = oC.get();
                customer.setVerified(true);
                return customerRepository.save(customer);
            }else{
                return oC.get();
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Customer find(Long id) {
        Optional<Customer> cO = customerRepository.findById(id);
        return cO.isPresent()? cO.get(): null;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = this.findByEmail(username);
        if(Objects.isNull(customer))
            throw new UsernameNotFoundException(messageSource.getMessage("customer.error.notFound", new Object[]{username}, LocaleContextHolder.getLocale() ));
        return customer;
    }


}
