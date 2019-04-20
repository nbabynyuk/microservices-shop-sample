package com.nb.orders.remote;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "payments", fallbackFactory = PaymentClientFallbackFactory.class)
public interface PaymentClient {

  @RequestMapping(method = POST, value = "/api/payments", consumes = "application/json")
  OperationResult process(PaymentRequest request);


  @RequestMapping(method = PUT, value = "/api/payments/{uuid}/rollback", consumes = "application/json")
  OperationResult executeRollback(@PathVariable String uuid);

}
