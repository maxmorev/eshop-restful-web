package ru.maxmorev.restful.eshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maxmorev.restful.eshop.config.ManagerConfig;
import ru.maxmorev.restful.eshop.feignclient.MailService;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedAdminTemplate;
import ru.maxmorev.restful.eshop.feignclient.domain.OrderPaymentConfirmedTemplate;
import ru.maxmorev.restful.eshop.feignclient.domain.VerifyEmailTemplate;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final MailService mailService;
    private final ManagerConfig managerConfig;

    @Override
    public void emailVerification(String email, String name, String code) {
        mailService.sendTemplate(
                new VerifyEmailTemplate()
                        .create(email,
                                name,
                                code
                        ));
    }

    @Override
    public void orderPaymentConfirmation(String email, String name, long orderId) {
        mailService.sendTemplate(
                new OrderPaymentConfirmedTemplate()
                        .create(email,
                                name,
                                orderId));
        mailService.sendTemplate(
                new OrderPaymentConfirmedAdminTemplate()
                        .create(
                                managerConfig.getEmail(),
                                orderId
                        ));
    }
}
