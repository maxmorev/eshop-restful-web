package ru.maxmorev.restful.eshop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "mymail")
@PropertySource("file:/opt/mail.properties")
@Getter
@Setter
public class MailConfiguration {

   private String smtp;
   private String port;
   private String username;
   private String password;

}
