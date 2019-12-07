package ru.maxmorev.restful.eshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;

@Profile("test")
@Configuration
public class MailTestConfig {

    @Bean
    public JavaMailSender mailSender() {
        return new MockMailSender();
    }

}
