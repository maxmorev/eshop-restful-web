package ru.maxmorev.restful.eshop.services;

public interface MailService {

    public Boolean sendEmail(String to, String subject, String text);

}
