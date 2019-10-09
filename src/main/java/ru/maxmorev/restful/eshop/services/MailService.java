package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.domain.Mail;

public interface MailService {

    public Boolean sendPlainEmail(Mail mail);

}
