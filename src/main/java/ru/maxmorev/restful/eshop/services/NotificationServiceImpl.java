package ru.maxmorev.restful.eshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxmorev.restful.eshop.config.ManagerConfig;
import ru.maxmorev.restful.eshop.feignclient.MailService;
import ru.maxmorev.restful.eshop.feignclient.domain.MailSendResponse;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedAdminTemplate;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedTemplate;
import ru.maxmorev.restful.eshop.feignclient.domain.VerifyEmailTemplate;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final MailService mailService;
    private final ManagerConfig managerConfig;

    @Override
    public MailSendResponse emailVerification(String email, String name, String code) {
        return mailService.sendTemplate(
                new VerifyEmailTemplate()
                        .create(email,
                                name,
                                code
                        ));
    }

    @Override
    public MailSendResponse orderPaymentConfirmation(String email, String name, long orderId) {
        mailService.sendTemplate(
                new OrderPaymentConfirmedAdminTemplate()
                        .create(
                                managerConfig.getEmail(),
                                orderId
                        ));
        return mailService.sendTemplate(
                new OrderPaymentConfirmedTemplate()
                        .create(email,
                                name,
                                orderId));
    }
}
