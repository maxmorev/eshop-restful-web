package ru.maxmorev.restful.eshop.feignclients;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.restful.eshop.feignclient.MailService;
import ru.maxmorev.restful.eshop.feignclient.domain.VerifyEmailTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DisplayName("Integration MailService test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 4555)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    public void verifyEmailSendSuccessTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mailSend.ok.json")));

        var response = mailService
                .sendTemplate(new VerifyEmailTemplate()
                        .create("maxmorev@gmail.com",
                                "ZfGT",
                                "Maxim Morev"));
        assertNotNull(response);
        assertEquals("0102017092c7d656-417dd273-459f-40f4-96e9-c291686d374e-000000",
                response.getMessageId());
    }

    @Test(expected = FeignException.class)
    public void verifyEmailSendFailTest() {
        stubFor(post(urlEqualTo("/send/template/"))
                .willReturn(
                        aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                ));

        var response = mailService
                .sendTemplate(new VerifyEmailTemplate()
                        .create("maxmorev@gmail.com",
                                "ZfGT",
                                "Maxim Morev"));
    }

}
