package ru.maxmorev.restful.eshop.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxmorev.restful.eshop.annotation.PaymentProvider;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.OrderPaymentConfirmation;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (OrderPurchaseController) test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class})
public class OrderPurchaseControllerTest {

    private static final Long APPROVED_ORDER_ID = 25L;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should confirm order")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void confirmOrderTest() throws Exception {
        OrderPaymentConfirmation opc = OrderPaymentConfirmation
                .builder()
                .paymentId("3HW05364355651909")
                .orderId(16L)
                .paymentProvider(PaymentProvider.Paypal.name())
                .build();
        mockMvc.perform(post(Constants.REST_CUSTOMER_URI + "order/confirm/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(opc.toString())
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")));

    }

    @Test
    @DisplayName("Should except validation errors: Wrond orderId")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void confirmOrderWrongOrderIdErrorTest() throws Exception {
        OrderPaymentConfirmation opc = OrderPaymentConfirmation
                .builder()
                .paymentId("3HW05364355651909")
                .orderId(166L)
                .paymentProvider(PaymentProvider.Paypal.name())
                .build();
        mockMvc.perform(post(Constants.REST_CUSTOMER_URI + "order/confirm/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(opc.toString())
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")));

    }

    @Test
    @DisplayName("Should except validation errors: Invalid order status")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void confirmOrderWrongOrderStatusErrorTest() throws Exception {
        OrderPaymentConfirmation opc = OrderPaymentConfirmation
                .builder()
                .paymentId("3HW05364355651909")
                .orderId(25L)
                .paymentProvider(PaymentProvider.Paypal.name())
                .build();
        mockMvc.perform(post(Constants.REST_CUSTOMER_URI + "order/confirm/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(opc.toString())
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Invalid order status")));

    }

    @Test
    @DisplayName("Should return customer's order list")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void customerOrderListTest() throws Exception {
        mockMvc.perform(get(Constants.REST_CUSTOMER_URI + "order/list/10")
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerId", is(10)))
                .andExpect(jsonPath("$[0].status", is("PAYMENT_APPROVED")));
    }

    @Test
    @DisplayName("Should return order by id and customer.id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void customerOrderTest() throws Exception {
        mockMvc.perform(get(Constants.REST_CUSTOMER_URI + "order/25/customer/10/")
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(25)))
                .andExpect(jsonPath("$.customerId", is(10)))
                .andExpect(jsonPath("$.status", is("PAYMENT_APPROVED")));
    }

    @Test
    @DisplayName("Should return error by id and customer.id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void customerOrderErrorTest() throws Exception {
        mockMvc.perform(get(Constants.REST_CUSTOMER_URI + "order/25/customer/15/")
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("No such order")));
    }


    @Test
    @DisplayName("Should expect error with wrong Authorities")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void prepareToShipException() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI + "order/PREPARING_TO_SHIP/" + APPROVED_ORDER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    @DisplayName("Should change order status")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void prepareToShipOk() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI + "order/PREPARING_TO_SHIP/" + APPROVED_ORDER_ID)
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("Should expect error with invalid order id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void prepareToShipInvalidIdTest() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI + "order/PREPARING_TO_SHIP/" + 2929)
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Invalid order id")));
    }

    @Test
    @DisplayName("Should expect error with invalid Status")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void prepareToShipInvalidStatusTest() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI + "order/PREPARING_TO_ROCK/" + APPROVED_ORDER_ID)
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Invalid status")));
    }

}
