package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static reactor.core.publisher.Mono.just;

public class PaymentsRemoteRepository {

  public static final String PAYMENTS_URI = "/api/payments";
  private final WebClient webClient;

  public PaymentsRemoteRepository(WebClient webClient) {
    this.webClient = webClient;
  }
  
  public Mono<OperationResult> process(PaymentRequest request){
    return webClient
        .post()
        .uri(PAYMENTS_URI)
        .body(BodyInserters.fromProducer(just(request),
            PaymentRequest.class
        ))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
        .exchange()
        .map(r -> r.bodyToMono(OperationResult.class))
        .flatMap( x -> x);
  }
  
  public OperationResult executeRollback(String uuid){
    return new OperationResult(UUID.randomUUID().toString()); 
  }

}
