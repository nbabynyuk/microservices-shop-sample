package com.nb.payments.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nb.payments.entity.PaymentData;
import com.nb.payments.functional.PaymentController;
import com.nb.payments.repo.PaymentRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(PaymentController.class)
public class PaymentAppTest {

  @MockBean
  private PaymentRepo paymentRepo;

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testProcessPayment() {

    when(paymentRepo.insert(any(PaymentData.class))).
        then((Answer<Mono<PaymentData>>) invocationOnMock -> {
          PaymentData pd = new PaymentData();
          pd.setUuid("xxx-111");
          return Mono.just(pd);
        });

    String value = "{ "
        + " \"paymentFrom\":{"
        + "      \"cardNumber\":\"xxxx-yyyy-zzzz\""
        + "     ,\"expireAt\":\"02/19\""
        + "     ,\"cvcode\":\"111\""
        + " },"
        + " \"paymentTo\":\"999-777-0\","
        + " \"amount\":50.322"
        + " }";

     webTestClient.post()
        .uri("/api/payments")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(value), String.class)
        .exchange()
        .expectStatus().isCreated();

  }

}
