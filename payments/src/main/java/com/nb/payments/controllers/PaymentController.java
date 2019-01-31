package com.nb.payments.controllers;

import com.nb.payments.entity.PaymentData;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController {


  @PostMapping("/orders")
  public Mono<String> processOrder(@Valid @RequestBody PaymentData paymentData ){
    return Mono.just(UUID.randomUUID().toString());
  }

}
