package ru.maxmorev.restful.eshop.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.controllers.ShoppingCartController;
import ru.maxmorev.restful.eshop.rest.request.RequestShoppingCartSet;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (ShoppingCartController) test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class})
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should get shopping cart by id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getShoppingCartTest() throws Exception {
        mockMvc.perform(get(Constants.REST_PUBLIC_URI + "shoppingCart/id/11"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.shoppingSet").isArray())
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.shoppingSet[0].amount", is(2)));
        //.andExpect(jsonPath("$.message", is("Success")));
    }

    @Test
    @DisplayName("Should increment amount of branch in shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void addToShoppingCartSetTest() throws Exception {
        RequestShoppingCartSet rscs = RequestShoppingCartSet
                .builder()
                .amount(1)
                .branchId(5L)
                .shoppingCartId(11L)
                .build();

        mockMvc.perform(post(Constants.REST_PUBLIC_URI + ShoppingCartController.SHOPPING_CART)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rscs.toString()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.shoppingSet").isArray())
                .andExpect(jsonPath("$.shoppingSet[0].amount", is(3)))
                .andExpect(jsonPath("$.id", is(11)));
    }

    @Test
    @DisplayName("Should decrement amount of branch in shopping cart")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void removeFromShoppingCartSetTest() throws Exception {
        RequestShoppingCartSet rscs = RequestShoppingCartSet
                .builder()
                .amount(1)
                .branchId(5L)
                .shoppingCartId(11L)
                .build();

        mockMvc.perform(delete(Constants.REST_PUBLIC_URI + ShoppingCartController.SHOPPING_CART)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rscs.toString()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.shoppingSet").isArray())
                .andExpect(jsonPath("$.shoppingSet[0].amount", is(1)))
                .andExpect(jsonPath("$.id", is(11)));
    }

    @Test
    @DisplayName("Should expect validation error in shopping cart id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void addToSCSShoppingCartIdValidationTest() throws Exception {
        RequestShoppingCartSet rscs = RequestShoppingCartSet
                .builder()
                .amount(1)
                .branchId(5L)
                .shoppingCartId(23L)
                .build();

        mockMvc.perform(post(Constants.REST_PUBLIC_URI + ShoppingCartController.SHOPPING_CART)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rscs.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors[0].field", is("shoppingCartId")));
    }

    @Test
    @DisplayName("Should expect validation error in branch id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void addToSCSBranchIdValidationTest() throws Exception {
        RequestShoppingCartSet rscs = RequestShoppingCartSet
                .builder()
                .amount(1)
                .branchId(50L)
                .shoppingCartId(22L)
                .build();

        mockMvc.perform(post(Constants.REST_PUBLIC_URI + ShoppingCartController.SHOPPING_CART)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rscs.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors[0].field", is("branchId")));
    }



}
