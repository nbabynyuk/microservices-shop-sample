package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

public class StockRemoteRepository {

  private final WebClient webClient;

  public StockRemoteRepository(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<OperationResult> processShipmentRequest(ShipmentRequest request) {
    return webClient
        .post()
        .uri("/api/delivery")
        .body(BodyInserters.fromProducer(just(request),
            ShipmentRequest.class
        ))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
        .exchange()
        .map(r -> r.bodyToMono(OperationResult.class))
        .flatMap( x -> x);
  }

}
