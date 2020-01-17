package ru.maxmorev.restful.eshop.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.config.OrderConfiguration;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.services.CustomerService;
import ru.maxmorev.restful.eshop.services.OrderPurchaseService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class})
@DisplayName("Integration CustomerOrderRepository Test")
public class CustomerOrderRepositoryTest {
    @Autowired
    private OrderPurchaseService orderPurchaseService;
    @Autowired
    OrderConfiguration orderConfiguration;
    @Autowired
    CustomerOrderRepository customerOrderRepository;
    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("should test value of order expired date interval in minutes")
    public void orderExpiredMinutesValueTest() {
        assertNotNull(orderConfiguration.getOrderExpiredMinutes());
    }

    @Test
    @Transactional
    @DisplayName("should test list of expired orders")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findExpiredOrdersTest() {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        customer.ifPresent(c -> orderPurchaseService.createOrderFor(c));

        assertEquals(2, customerOrderRepository.count());

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -orderConfiguration.getOrderExpiredMinutes());
        Date teenMinutesFromNow = now.getTime();
        List<CustomerOrder> expiredOrders = customerOrderRepository.findByCustomerOrderStatus(CustomerOrderStatus.AWAITING_PAYMENT, teenMinutesFromNow);

        assertEquals(1, expiredOrders.size());
    }
}
