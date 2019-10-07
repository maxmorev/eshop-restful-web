package ru.maxmorev.restful.eshop.services;

import org.apache.tiles.request.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("mailService")
public class MailServiceImpl implements MailService{

    public JavaMailSender javaMailSender;
    private Environment environment;

    @Autowired public void setJavaMailSender(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Autowired public void setEnvirenment(Environment env){
        this.environment = env;
    }

    @Override
    public Boolean sendEmail(String to, String subject, String text) {

        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom( environment.getProperty("mymail.user") );
        message.setTo(to);
        message.setSubject( subject);
        message.setText(text);
        javaMailSender.send(message);

        return true;
    }
}
