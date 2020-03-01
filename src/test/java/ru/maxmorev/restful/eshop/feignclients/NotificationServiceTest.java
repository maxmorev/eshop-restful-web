package ru.maxmorev.restful.eshop.feignclients;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.restful.eshop.services.NotificationService;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DisplayName("Integration NotificationService test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 4555)
public class NotificationServiceTest {
    private static final String RESP_ID = "0102017092c7d656-417dd273-459f-40f4-96e9-c291686d374e-000000";
    @Autowired
    NotificationService notificationService;

    @Test
    public void emailVerificationTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mailSend.ok.json")));
        var resp = notificationService.emailVerification(
                "maxmorev@gmail.com",
                "Maxim Morev",
                "CoDe");
        assertEquals(RESP_ID,
                resp.getMessageId());
    }

    @Test(expected = FeignException.class)
    public void emailVerificationFailTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())));
        notificationService.emailVerification(
                "maxmorev@gmail.com",
                "Maxim Morev",
                "CoDe");
    }

    @Test
    public void orderPaymentConfirmationTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mailSend.ok.json")));
        var resp = notificationService.orderPaymentConfirmation(
                "maxmorev@gmail.com",
                "Maxim Morev",
                100L);
        assertEquals(RESP_ID,
                resp.getMessageId());
    }

    @Test(expected = FeignException.class)
    public void orderPaymentConfirmationFailTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())));
        notificationService.orderPaymentConfirmation(
                "maxmorev@gmail.com",
                "Maxim Morev",
                100L);
    }

}
