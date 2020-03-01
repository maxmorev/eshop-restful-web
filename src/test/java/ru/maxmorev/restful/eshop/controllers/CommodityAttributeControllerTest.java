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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.rest.Constants;
import ru.maxmorev.restful.eshop.rest.controllers.CommodityAttributeController;
import ru.maxmorev.restful.eshop.rest.response.Message;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (CommodityAttributeController) test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class})
public class CommodityAttributeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return available attribute data types")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getAvailableAttributeDataTypesTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(Constants.REST_PUBLIC_URI + CommodityAttributeController.ATTRIBUTE_DATA_TYPES))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", is("String")));
    }

    @Test
    @DisplayName("Should return available attributes for type")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getAttributesTest() throws Exception {
        mockMvc.perform(get(Constants.REST_PUBLIC_URI + "attributes/" + 1))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name", is("size")));
    }

    @Test
    @DisplayName("Should remove attribute by id")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void deletePropertyValueTest() throws Exception {
        mockMvc.perform(delete(Constants.REST_PRIVATE_URI + "attributeValue/3")
                .with(user("admin")
                        .password("pass")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Message.SUCCES)));
    }

    @Test
    @DisplayName("Should create attribute")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createAttributeTest() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI+"attribute/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeId\":1, \"name\":\"size\", \"dataType\":\"String\", \"measure\":null, \"value\":\"m\"}")
                .with(user("admin")
                        .password("pass")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Message.SUCCES)));
    }

    @Test
    @DisplayName("Should create attribute with error")
    @SqlGroup({
            @Sql(value = "classpath:db/purchase/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/purchase/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void createAttributeErrorTest() throws Exception {
        mockMvc.perform(post(Constants.REST_PRIVATE_URI+"attribute/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeId\":1, \"name\":\"size\", \"dataType\":\"string\", \"measure\":null, \"value\":\"m\"}")
                .with(user("admin")
                        .password("pass")
                        .authorities((GrantedAuthority) () -> "ADMIN")))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.status", is(Message.ERROR)))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors").isArray());

    }


}
