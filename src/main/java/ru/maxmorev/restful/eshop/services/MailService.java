package ru.maxmorev.restful.eshop.services;

import ru.maxmorev.restful.eshop.domain.Mail;

import javax.validation.Valid;

public interface MailService {

    public boolean sendPlainEmail(@Valid Mail mail);

}
