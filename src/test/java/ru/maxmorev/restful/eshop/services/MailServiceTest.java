package ru.maxmorev.restful.eshop.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.restful.eshop.config.MailTestConfig;
import ru.maxmorev.restful.eshop.config.ServiceConfig;
import ru.maxmorev.restful.eshop.config.ServiceTestConfig;
import ru.maxmorev.restful.eshop.domain.Mail;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DisplayName("Mail Service Test")
@SpringJUnitConfig(classes = {ServiceTestConfig.class, ServiceConfig.class, MailTestConfig.class})
public class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    @DisplayName("Should send email")
    public void sendPlainEmailTest() {
        Mail mail = Mail
                .builder()
                .subject("Sending email test")
                .text("Hello freak bitches")
                .to("maxmorev@titsonfire.store")
                .build();
        boolean meailSended = mailService.sendPlainEmail(mail);
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException while send email")
    public void sendPlainEmailTestValidationError() {
        Mail mail = Mail
                .builder()
                .text("Hello freak bitches")
                .to("maxmorev@titsonfire.store")
                .build();
        assertThrows(ConstraintViolationException.class, () -> {
            boolean meailSended = mailService.sendPlainEmail(mail);
        });
    }

}
