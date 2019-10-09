package ru.maxmorev.restful.eshop.services;

import org.apache.tiles.request.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.maxmorev.restful.eshop.domain.Mail;

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
    public Boolean sendPlainEmail(Mail mail) {

        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom( environment.getProperty("mymail.user") );
        message.setTo(mail.getTo());
        message.setSubject( mail.getSubject() );
        message.setText( mail.getText() );
        javaMailSender.send(message);

        return true;
    }
}
