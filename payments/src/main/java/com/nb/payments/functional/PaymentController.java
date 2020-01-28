package com.nb.payments.functional;

import com.nb.common.OperationResult;
import com.nb.payments.entity.PaymentData;
import com.nb.payments.repo.PaymentRepo;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
//TODO: delete me
public class PaymentController {

  private final PaymentRepo paymentRepo;

  @Autowired
  public PaymentController(PaymentRepo paymentRepo) {
    this.paymentRepo = paymentRepo;
  }

//  @PostMapping(value = "/api/payments",
//      produces = "application/json",
//      consumes = "application/json")
//  @ResponseStatus(HttpStatus.CREATED)
//  public Mono<OperationResult> processOrder(@Valid @RequestBody PaymentData paymentData ){
//    return Mono.just(paymentData).
//        map(x -> {
//          x.setUuid(UUID.randomUUID().toString());
//          return x;
//        }).flatMap(paymentRepo::insert)
//          .map( storedPayment ->
//            new OperationResult(storedPayment.getUuid())
//          );
//  }

}
