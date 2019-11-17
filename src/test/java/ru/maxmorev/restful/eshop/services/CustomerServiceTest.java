package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.AuthorityValues;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerAuthority;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.entities.ShoppingCart;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
@DisplayName("Integration Customer Service Test")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("should create customer")
    @Transactional
    public void testCreateCustomerAndVerifyByEmail() {
        Customer customer = Customer
                .builder()
                .email("test@titsonfire.store")
                .fullName("Maxim Morev")
                .address("Test Address")
                .authorities(AuthorityValues.ADMIN.name())
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        customer = customerService.createCustomerAndVerifyByEmail(customer);
        em.flush();
        log.info("Customer#VerifyCode {}", customer.getVerifyCode());
        log.info("Customer#passwdord {}", customer.getPassword());
        assertFalse(customer.getVerifyCode().isEmpty());
        assertTrue(customer.getAuthorities().contains(new CustomerAuthority(AuthorityValues.CUSTOMER)));
        assertFalse(customer.getAuthorities().contains(new CustomerAuthority(AuthorityValues.ADMIN)));
    }

    @Test
    @DisplayName("should throw exception while create customer couse address is null")
    @Transactional
    public void testErrorCreateCustomerAndVerifyByEmail() {
        Customer customer = Customer
                .builder()
                .email("test@titsonfire.store")
                .fullName("Maxim Morev")
                .authorities(AuthorityValues.ADMIN.name())
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        customer = customerService.createCustomerAndVerifyByEmail(customer);
        assertThrows(javax.validation.ConstraintViolationException.class, em::flush);
    }

    @Test
    @DisplayName("sending mail error, creation fail")
    @Transactional
    public void testErrorSendMail() {
        Customer customer = Customer
                .builder()
                .email("error@error.send")
                .fullName("Maxim Morev")
                .address("Address test")
                .authorities(AuthorityValues.ADMIN.name())
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        assertThrows(org.springframework.mail.MailPreparationException.class, () -> customerService.createCustomerAndVerifyByEmail(customer));
        //assertThrows(javax.validation.ConstraintViolationException.class, em::flush);
    }

    @Test
    @DisplayName("should find customer by email")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindByEmail() {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        assertTrue(customer.isPresent());
    }

    @Test
    @DisplayName("should find customer by id")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindById() {
        // Optional<Customer> find(Long id); 10
        Optional<Customer> customer = customerService.findById(10L);
        assertTrue(customer.isPresent());
        assertFalse(customer.get().getVerified());
    }

    @Test
    @Transactional
    @DisplayName("should verify customer by id and verify code")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testVerify() {
        Optional<Customer> customer = customerService.verify(10L, "TKYOC");
        em.flush();
        assertTrue(customer.isPresent());
        assertTrue(customer.get().getVerified());
    }

    @Test
    @Transactional
    @DisplayName("should not verify customer by id and verify code")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testVerifyError() {
        Optional<Customer> customer = customerService.verify(10L, "TKYOX");
        em.flush();
        assertFalse(customer.isPresent());
    }

    @Test
    @Transactional
    @DisplayName("should update customer info")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateInfoTest() {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        assertTrue(customer.isPresent());
        Customer c = customer.get();
        c.setCountry("Canada");
        c.setCity("Toronto");
        Customer result = customerService.updateInfo((CustomerInfo) c);
        em.flush();
        assertEquals("Canada", result.getCountry());
        assertEquals("Toronto", result.getCity());
    }

    @Test
    @Transactional
    @DisplayName("should update customer: create shopping cart and set it")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCustomerTest() {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        assertTrue(customer.isPresent());

        ShoppingCart shoppingCart = new ShoppingCart();
        Customer c = customer.get();
        c.setShoppingCart(shoppingCart);
        customerService.update(c);

        em.flush();

        customer = customerService.findByEmail("test@titsonfire.store");
        assertTrue(customer.get().getShoppingCart() != null);
        assertTrue(customer.get().getShoppingCart().getId() != null);

    }


}
