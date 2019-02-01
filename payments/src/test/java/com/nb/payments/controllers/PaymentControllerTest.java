package com.nb.payments.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@WebFluxTest(PaymentController.class)
public class PaymentControllerTest {

  @Autowired
  private WebTestClient webTestClient;


  @Test
  public void testProcessPayment() throws Exception {

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
        .uri("/payments")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(value), String.class)
        .exchange()
        .expectStatus().isCreated();

  }

}
