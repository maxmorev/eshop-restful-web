package ru.maxmorev.restful.eshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("file:/opt/mail.properties")
public class MailConfig {
    private final static Logger logger = LoggerFactory.getLogger(MailConfig.class);

    private Environment environment;

    @Autowired public void setEnvirenment(Environment env){
        this.environment = env;
    }


    @Bean
    public JavaMailSender getJavaMailSender() {
        logger.info("Creating mail service");
        logger.info("mymail.smtp.port - " + environment.getProperty("mymail.smtp.port"));
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost( environment.getProperty("mymail.smtp") );

        mailSender.setPort( Integer.valueOf( environment.getProperty("mymail.smtp.port")) );
        mailSender.setUsername( environment.getProperty("mymail.user") );
        mailSender.setPassword( environment.getProperty("mymail.user.password") );
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.mime.charset", "utf8");

        return mailSender;
    }

}
