package com.nb.payments.controller;

import com.nb.payments.AppRoutingConfig;
import com.nb.payments.entity.PaymentData;
import com.nb.payments.functional.PaymentHandlerFunctions;
import com.nb.payments.repo.PaymentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    AppRoutingConfig.class,
    PaymentHandlerFunctions.class})
@WebFluxTest
public class PaymentAppTest {

    @MockBean
    private PaymentRepo paymentRepo;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testProcessPayment() throws Exception {

        doReturn(createMockOfResponseData()).when(paymentRepo).save(any(PaymentData.class));

        webTestClient.post()
            .uri("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(readMockData()), String.class)
            .exchange()
            .expectStatus()
                .isCreated()
            .expectBody()
                .jsonPath("$.uuid").exists();
    }

    private Mono<PaymentData> createMockOfResponseData() {
        PaymentData pd = new PaymentData();
        pd.setUuid("xxx-111");
        return Mono.just(pd);
    }

    private String readMockData() throws IOException {
        InputStream resourceAsStream = PaymentAppTest.class
            .getClassLoader()
            .getResourceAsStream("payment-request.json");
        assert resourceAsStream != null;
        return new String (resourceAsStream.readAllBytes());
    }
}
