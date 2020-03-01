package ru.maxmorev.restful.eshop.services;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.entities.CommodityBranch;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerOrder;
import ru.maxmorev.restful.eshop.entities.Purchase;
import ru.maxmorev.restful.eshop.repository.CommodityBranchRepository;
import ru.maxmorev.restful.eshop.repository.CustomerOrderRepository;
import ru.maxmorev.restful.eshop.rest.response.CustomerOrderDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
@DisplayName("Integration Purchase Service Test")
public class OrderPurchaseServiceTest {

    private static final Long APPROVED_ORDER_ID = 25L;
    @Autowired
    private OrderPurchaseService orderPurchaseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CommodityService commodityService;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    CommodityBranchRepository commodityBranchRepository;
    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Value
    public static class ShoppingCartInfo {
        long branchId;
        int branchAmount;
        int shoppingCartAmount;

        public ShoppingCartInfo(long branchId, int branchAmount, int shoppingCartAmount) {
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
    public void createOrderForTest() {

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
            Assertions.assertEquals(CustomerOrderStatus.AWAITING_PAYMENT, co.getStatus());
            assertEquals(branchInfo.stream().mapToInt(ShoppingCartInfo::getShoppingCartAmount).sum(), co.getPurchases().stream().mapToInt(Purchase::getAmount).sum());
            branchInfo.forEach(branch -> {
                Optional<CommodityBranch> fromDb = commodityService.findBranchById(branch.getBranchId());
                log.info("Branch#{} -> amount {} -> shopping cart amount {}", branch.getBranchId(),
                        branch.getBranchAmount(),
                        branch.getShoppingCartAmount());
                assertEquals(fromDb.get().getAmount().intValue(),
                        branch.getBranchAmount() - branch.getShoppingCartAmount());


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
    public void createOrderForTestException() {

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
            assertThrows(IllegalArgumentException.class, () -> orderPurchaseService.createOrderFor(c));
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
    public void confirmPaymentOrderTest() {
        Optional<CustomerOrder> order = orderPurchaseService.findOrder(16L);
        assertTrue(order.isPresent());
        order.ifPresent(o -> {
            /* check if the shopping cart is Not Empty*/
            assertFalse(o.getCustomer().getShoppingCart().getShoppingSet().isEmpty());
            Assertions.assertEquals(CustomerOrderStatus.AWAITING_PAYMENT, o.getStatus());

            orderPurchaseService.confirmPaymentOrder(o, PaymentProvider.Paypal, "3HW05364355651909");
            em.flush();

            Optional<CustomerOrder> orderUpdate = orderPurchaseService.findOrder(16L);
            assertNotNull(orderUpdate.get().getPaymentID());
            /* checks status change for expected */
            Assertions.assertEquals(
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
    public void findOrderTest() {
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
    public void findCustomerOrdersTest() {
        List<CustomerOrder> orders = orderPurchaseService.findCustomerOrders(10L);
        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
    }

    @Test
    @Transactional
    @DisplayName("should clean expired orders")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void cleanExpiredOrdersTest() {
        Assert.assertEquals(2, customerOrderRepository.count());
        Optional<CommodityBranch> optionalBranch = commodityBranchRepository.findById(5L);
        assertTrue(optionalBranch.isPresent());
        CommodityBranch branch = optionalBranch.get();
        assertEquals(5, branch.getAmount().intValue());

        orderPurchaseService.cleanExpiredOrders();

        Assert.assertEquals(1, customerOrderRepository.count());
        optionalBranch = commodityBranchRepository.findById(5L);
        assertTrue(optionalBranch.isPresent());
        branch = optionalBranch.get();
        assertEquals(7, branch.getAmount().intValue());
    }

    @Test
    @Transactional
    @DisplayName("should change order status to PREPARING_TO_SHIP")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void confirmOrderPreparingToShipTest() {
        Optional<CustomerOrder> order = orderPurchaseService.findOrder(APPROVED_ORDER_ID);
        assertTrue(order.isPresent());
        Assertions.assertEquals(CustomerOrderStatus.PAYMENT_APPROVED, order.get().getStatus());
        CustomerOrder co = orderPurchaseService.setOrderStatus(APPROVED_ORDER_ID, CustomerOrderStatus.PREPARING_TO_SHIP);
        Assertions.assertEquals(CustomerOrderStatus.PREPARING_TO_SHIP, co.getStatus());
    }

    @Test
    @Transactional
    @DisplayName("should cancel order")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void cancelOrderTest() {
        //16
        CommodityBranch branch = commodityBranchRepository
                .findById(5L)
                .orElseThrow(IllegalAccessError::new);
        assertEquals(5, branch.getAmount().intValue());
        orderPurchaseService.cancelOrderByCustomer(16L);
        Assertions.assertEquals(7,
                commodityBranchRepository
                        .findById(5L)
                        .get()
                        .getAmount()
                        .intValue());
    }

    @Test
    @Transactional
    @DisplayName("should except change order status by customer")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void cancelOrderExceptionTest() {

        orderPurchaseService.cancelOrderByCustomer(APPROVED_ORDER_ID);

        orderPurchaseService.findOrder(APPROVED_ORDER_ID).ifPresent(o -> {
            assertEquals(CustomerOrderStatus.CANCELED_BY_CUSTOMER, o.getStatus());
        });

    }

    @Test
    @Transactional
    @DisplayName("should except correct list of orders dto for customer no orders with status=AWAITING_PAYMENT")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getCorrectOrderListDtoForCustomerWithActionTest() {

        List<CustomerOrderDto> orders = orderPurchaseService.findOrderListForCustomer(10L);
        assertEquals(1, orders.size());
        assertEquals(25L, orders.get(0).getId().longValue());
        assertEquals(1, orders.get(0).getActions().size());
        assertEquals(CustomerOrderStatus.CANCELED_BY_CUSTOMER.name(),
                orders.get(0).getActions().get(0).getAction());

    }

    @Test
    @Transactional
    @DisplayName("should except correct Page for admin")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getCorrectOrderPageForAdminTest() {

        var page = orderPurchaseService.getOrdersForAdmin(null, null, null, null);
        assertEquals(1, page.getTotalRecords());
        assertEquals(CustomerOrderStatus.PREPARING_TO_SHIP.name(),
                page.getOrderData()
                        .get(0)
                        .getActions()
                        .get(0)
                        .getAction());

    }




}
