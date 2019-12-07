package ru.maxmorev.restful.eshop.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Arrays;

@Slf4j
public class MockMailSender extends JavaMailSenderImpl {

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {

        log.info( "sending test from: {}", simpleMessage.getFrom());
        log.info("sending to {}", simpleMessage.getTo());
        log.info("simpleMessage.getTo().length", simpleMessage.getTo().length);
        if (Arrays.asList(simpleMessage.getTo()).stream().anyMatch(s->s.contains("@error"))){
            log.info("Error while sending email, to is null!");
            throw new MailPreparationException("Error while sending email, to is null");
        }

    }

}
