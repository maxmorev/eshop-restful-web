package ru.maxmorev.restful.eshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * configuration takes values from environment variables
 */
@Getter
@Setter
@Configuration
public class MailConfiguration {

   @Value("${ESHOP_SMTP}")
   private String smtp;
   @Value("${ESHOP_SMTP_PORT}")
   private String port;
   @Value("${ESHOP_SMTP_USERNAME}")
   private String username;
   @Value("${ESHOP_SMTP_PASSWORD}")
   private String password;

   @PostConstruct
   public void isEnvironmentVarsSet() throws Exception {
      Objects.requireNonNull(smtp);
      Objects.requireNonNull(port);
      Objects.requireNonNull(username);
      Objects.requireNonNull(password);
   }

}
