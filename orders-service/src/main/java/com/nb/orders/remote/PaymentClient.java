package com.nb.orders.remote;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.nb.common.CreditCardDTO;
import com.nb.common.PaymentRequest;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("payments")
public interface PaymentClient {

  @RequestMapping(method = POST, value = "/api/payments", consumes = "application/json")
  void process(PaymentRequest request);

}
