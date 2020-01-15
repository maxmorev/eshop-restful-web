package ru.maxmorev.restful.eshop.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (OrderPurchaseController) test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class})
public class OrderPurchaseControllerTest {

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
        mockMvc.perform(post(Constants.REST_CUSTOMER_URI+"order/confirm/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(opc.toString())
                .with(user("customer")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Success")));

    }

}
