package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.maxmorev.restful.eshop.config.MailConfiguration;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
@DisplayName("Mail configuration Test")
public class MailConfigurationTest {

    @Autowired
    MailConfiguration mailConfiguration;

    @Test
    @DisplayName("should be configured")
    public void testConfiguration(){
        assertNotNull(mailConfiguration.getUsername());
        assertNotNull(mailConfiguration.getPassword());
        assertNotNull(mailConfiguration.getSmtp());
        assertNotNull(mailConfiguration.getPort());
    }
}
