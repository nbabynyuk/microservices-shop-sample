package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

public class PaymentsRemoteRepository {

  private final WebClient webClient;

  public PaymentsRemoteRepository(WebClient webClient) {
    this.webClient = webClient;
  }
  
  public OperationResult process(PaymentRequest request){
    return new OperationResult(UUID.randomUUID().toString());
  }
  
  public OperationResult executeRollback(String uuid){
    return new OperationResult(UUID.randomUUID().toString());
  }

}
