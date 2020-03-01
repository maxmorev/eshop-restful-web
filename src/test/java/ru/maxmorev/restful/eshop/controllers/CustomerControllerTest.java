package ru.maxmorev.restful.eshop.controllers;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.entities.Customer;
import ru.maxmorev.restful.eshop.entities.CustomerInfo;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.request.CustomerVerify;
import ru.maxmorev.restful.eshop.rest.response.CustomerDTO;
import ru.maxmorev.restful.eshop.services.CustomerService;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 4555)
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (CustomerController) test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("Should create customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void createCustomerTest() throws Exception {
        stubFor(WireMock.post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mailSend.ok.json")));

        Customer customer = Customer
                .builder()
                .email("test@titsonfire.store")
                .fullName("Maxim Morev")
                .address("Test Address")
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("test@titsonfire.store")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("Should except error while create customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void createCustomerUniqueErrorTest() throws Exception {
        assertTrue(customerService.findByEmail("test@titsonfire.store").isPresent());
        Customer customer = Customer
                .builder()
                .email("test@titsonfire.store")
                .fullName("Maxim Morev")
                .address("Test Address")
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Internal storage error")));
    }

    @Test
    @DisplayName("Should except validation error while create customer from RequestBody")
    @Transactional
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createCustomerValidationErrorTest() throws Exception {
        assertTrue(customerService.findByEmail("test@titsonfire.store").isPresent());
        Customer customer = Customer
                .builder()
                .email("test2@titsonfire.store")
                .fullName("Maxim Morev")
                .address("")
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors[0].field", is("address")));
    }

    @Test
    @DisplayName("Should except validation errors while create customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createCustomerValidationErrorFullListTest() throws Exception {
        Customer customer = Customer
                .builder()
                .email("")
                .fullName("")
                .address("")
                .postcode("")
                .city("")
                .country("")
                .password("")
                .build();
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(8)))
        ;
    }

    @Test
    @DisplayName("Should except email pattern validation error while create customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/customer/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/customer/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createCustomerEmailValidationErrorTest() throws Exception {
        //assertTrue(customerService.findByEmail("test@titsonfire.store").isPresent());
        Customer customer = Customer
                .builder()
                .email("notvalid@titsonfire")
                .fullName("Maxim Morev")
                .address("Correct Address")
                .postcode("111123")
                .city("Moscow")
                .country("Russia")
                .password("helloFreakBitches")
                .build();
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customer.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors[0].field", is("email")));
    }

    @Test
    @DisplayName("Should update customer info from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCustomerTest() throws Exception {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        CustomerInfo i = customer.get();
        assertEquals("Russia", i.getCountry());
        assertEquals("Moscow", i.getCity());
        i.setCountry("Canada");
        i.setCity("Toronto");
        mockMvc.perform(put(Constants.REST_CUSTOMER_URI + "update/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CustomerDTO.of(i).toString())
                .with(user("test@titsonfire.store")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Toronto")))
                .andExpect(jsonPath("$.country", is("Canada")));

    }

    @Test
    @DisplayName("Should not update customer info from RequestBody and expected auth error")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCustomerAuthoritiesErrorTest() throws Exception {
        Optional<Customer> customer = customerService.findByEmail("test@titsonfire.store");
        CustomerInfo i = customer.get();
        assertEquals("Russia", i.getCountry());
        assertEquals("Moscow", i.getCity());
        i.setCountry("Canada");
        i.setCity("Toronto");
        mockMvc.perform(put(Constants.REST_CUSTOMER_URI + "update/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CustomerDTO.of(i).toString())
                .with(user("another-customer@titsonfire.store")
                        .password("customer")
                        .authorities((GrantedAuthority) () -> "CUSTOMER")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Not Authenticated")));

    }

    @Test
    @DisplayName("Should verify customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void verifyCustomerTest() throws Exception {
        //Constants.REST_PUBLIC_URI + "customer/verify/"
        CustomerVerify cv = new CustomerVerify();
        cv.setId(15L);
        cv.setVerifyCode("TKYOC");
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/verify/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cv.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified", is(true)));
    }

    @Test
    @DisplayName("Should fail verify customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void verifyCustomerFailTest() throws Exception {
        CustomerVerify cv = new CustomerVerify();
        cv.setId(15L);
        cv.setVerifyCode("FAILy");//incorrect verify code
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/verify/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cv.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified", is(false)));
    }

    @Test
    @DisplayName("Should expect error while verify customer from RequestBody")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void verifyCustomerUserNotFoundTest() throws Exception {
        CustomerVerify cv = new CustomerVerify();
        cv.setId(16L);
        cv.setVerifyCode("FAILy");//incorrect verify code
        mockMvc.perform(post(Constants.REST_PUBLIC_URI + "customer/verify/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cv.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Customer with id 16 not found")));
    }

}
