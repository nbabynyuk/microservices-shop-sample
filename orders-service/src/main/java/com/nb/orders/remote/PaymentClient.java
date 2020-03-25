package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "payments", fallbackFactory = PaymentClientFallbackFactory.class)
public interface PaymentClient {

  @PostMapping(value = "/api/payments", consumes = "application/json")
  OperationResult process(PaymentRequest request);


  @PutMapping(value = "/api/payments/{uuid}/rollback", consumes = "application/json")
  OperationResult executeRollback(@PathVariable String uuid);

}
