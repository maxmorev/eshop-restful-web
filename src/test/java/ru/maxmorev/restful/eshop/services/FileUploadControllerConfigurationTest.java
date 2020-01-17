package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.restful.eshop.config.FileUploadConfiguration;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DisplayName("FileUploadController configuration Test")
@SpringBootTest(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
public class FileUploadControllerConfigurationTest {

    @Autowired
    FileUploadConfiguration fileUploadConfiguration;

    @Test
    @DisplayName("FileUploadController env variables should be configured")
    public void testConfiguration() {
        log.info("endpoint {}", fileUploadConfiguration.getEndpoint());
        log.info("accessKey {}", fileUploadConfiguration.getAccessKey());
        assertNotNull(fileUploadConfiguration.getEndpoint());
        assertNotNull(fileUploadConfiguration.getAccessKey());
    }
}
