package ru.maxmorev.restful.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.maxmorev.restful.eshop.config.MailConfiguration;
import ru.maxmorev.restful.eshop.domain.Mail;

@Component("mailService")
public class MailServiceImpl implements MailService{

    public JavaMailSender javaMailSender;
    @Autowired
    MailConfiguration mailConfiguration;

    @Autowired public void setJavaMailSender(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }


    @Override
    public Boolean sendPlainEmail(Mail mail) {
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfiguration.getUsername());
        message.setTo(mail.getTo());
        message.setSubject( mail.getSubject() );
        message.setText( mail.getText() );
        javaMailSender.send(message);

        return true;
    }
}
