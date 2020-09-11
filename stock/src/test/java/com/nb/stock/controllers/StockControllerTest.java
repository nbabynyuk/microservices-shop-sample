package com.nb.stock.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@WebFluxTest(StockController.class)
public class StockControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void processShipmentRequest_ok() throws Exception {

    String value = loadResource();

    webTestClient.post()
        .uri("/api/delivery")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(value), String.class)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  private String loadResource() throws IOException {
    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("create-delivery.json");
    assert resourceAsStream != null;
    return new String(resourceAsStream.readAllBytes());
  }
}