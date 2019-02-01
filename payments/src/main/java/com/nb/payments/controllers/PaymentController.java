package com.nb.payments.controllers;

import com.nb.payments.entity.PaymentData;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController {


  @PostMapping("/payments")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<String> processOrder(@Valid @RequestBody PaymentData paymentData ){
    return Mono.just(UUID.randomUUID().toString());
  }

}
