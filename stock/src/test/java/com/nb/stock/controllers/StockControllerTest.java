package com.nb.stock.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(StockController.class)
public class StockControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void processShipmentRequest_ok() {

    String value = "{"
        + "\"address\":\"ljlfdsjlfds\","
        + "\"userId\": 5,"
        + "\"products\": [{"
        +   "\"productId\":12,"
        +   "\"quantity\":1"
        + " }]"
        + "}";

    webTestClient.post()
        .uri("/api/shipmentRequest")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(value), String.class)
        .exchange()
        .expectStatus().isCreated();

  }


}