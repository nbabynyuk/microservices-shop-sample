package com.nb.orders.remote;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.nb.common.CreditCardDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("users")
public interface UserClient {

  @RequestMapping(method = GET, value = "/user/{userId}/paymentDetails", consumes = "application/json")
  Optional<List<CreditCardDTO>> getPaymentDetails(Long userId);

}
