package ru.maxmorev.restful.eshop.services;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.annotation.CustomerOrderStatus;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
@DisplayName("Integration Purchase Service Test")
public class OrderPurchaseServiceTest {

    @Autowired
    private OrderPurchaseService orderPurchaseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CommodityService commodityService;
    @PersistenceContext
    private EntityManager em;

    @Value
    class ShoppingCartInfo{
        long branchId;
        int branchAmount;
        int shoppingCartAmount;
        public ShoppingCartInfo(long branchId, int branchAmount, int shoppingCartAmount){
            this.branchId = branchId;
            this.branchAmount = branchAmount;
            this.shoppingCartAmount = shoppingCartAmount;
        }
    }

    @Test
    @Transactional
    @DisplayName("should create order")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createOrderForTest(){

        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");

        customer.ifPresent(c -> {
            log.info("c.getShoppingCart().getShoppingSet().size {}", c.getShoppingCart().getShoppingSet().size());
            List<ShoppingCartInfo> branchInfo = c.getShoppingCart().getShoppingSet()
                    .stream()
                    .map(scs -> new ShoppingCartInfo(
                            scs.getBranch().getId(),
                            scs.getBranch().getAmount(),
                            scs.getAmount()))
                    .collect(Collectors.toList());

            CustomerOrder co = orderPurchaseService.createOrderFor(c);
            assertFalse(co.getPurchases().isEmpty());
            em.flush();
            assertEquals(CustomerOrderStatus.AWAITING_PAYMENT, co.getStatus());
            assertEquals(branchInfo.stream().mapToInt(ShoppingCartInfo::getShoppingCartAmount).sum(), co.getPurchases().stream().mapToInt(Purchase::getAmount).sum());
            branchInfo.forEach(branch->{
                Optional<CommodityBranch> fromDb = commodityService.findBranchById(branch.getBranchId());
                log.info("Branch#{} -> amount {} -> shopping cart amount {}", branch.getBranchId(),
                        branch.getBranchAmount(),
                        branch.getShoppingCartAmount());
                assertEquals(fromDb.get().getAmount().intValue(),
                        branch.getBranchAmount()-branch.getShoppingCartAmount());


            });

        });
    }

    @Test
    @Transactional
    @DisplayName("should throws exception while creating order")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createOrderForTestException(){

        Optional<Customer> customer = customerService.findByEmail("test-error@titsonfire.store");

        customer.ifPresent(c -> {
            log.info("c.getShoppingCart().getShoppingSet().size {}", c.getShoppingCart().getShoppingSet().size());
            List<ShoppingCartInfo> branchInfo = c.getShoppingCart().getShoppingSet()
                    .stream()
                    .map(scs -> new ShoppingCartInfo(
                            scs.getBranch().getId(),
                            scs.getBranch().getAmount(),
                            scs.getAmount()))
                    .collect(Collectors.toList());
            assertThrows(IllegalArgumentException.class, ()->orderPurchaseService.createOrderFor(c));
        });
    }

    @Test
    @Transactional
    @DisplayName("should check and confirm order")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void confirmPaymentOrderTest(){
        Optional<CustomerOrder> order = orderPurchaseService.findOrder(16L);
        assertTrue(order.isPresent());
        order.ifPresent(o -> {
            /* check if the shopping cart is Not Empty*/
            assertFalse(o.getCustomer().getShoppingCart().getShoppingSet().isEmpty());
            assertEquals(CustomerOrderStatus.AWAITING_PAYMENT, o.getStatus());

            orderPurchaseService.confirmPaymentOrder(o, PaymentProvider.Paypal, "3HW05364355651909");
            em.flush();

            Optional<CustomerOrder> orderUpdate = orderPurchaseService.findOrder(16L);
            assertNotNull(orderUpdate.get().getPaymentID());
            /* checks status change for expected */
            assertEquals(
                    CustomerOrderStatus.PAYMENT_APPROVED,
                    orderUpdate.get().getStatus()
                    );
            /* check if the shopping cart is Empty*/
            assertTrue(
                    customerService
                    .findById(o
                            .getCustomer()
                            .getId())
                    .get()
                    .getShoppingCart()
                    .getShoppingSet()
                    .isEmpty());
        });

    }

    @Test
    @Transactional
    @DisplayName("should find customer order by order ID")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findOrderTest(){
        Optional<CustomerOrder> order = orderPurchaseService.findOrder(16L);
        assertTrue(order.isPresent());
    }

    @Test
    @Transactional
    @DisplayName("should find customer orders")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findCustomerOrdersTest(){
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        assertTrue(customer.isPresent());
        List<CustomerOrder> orders = orderPurchaseService.findCustomerOrders(customer.get());
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

}
