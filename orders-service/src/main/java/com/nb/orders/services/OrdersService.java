package com.nb.orders.services;

import com.nb.common.PaymentRequest;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.UserClient;
import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrdersService {

  @Value("${orders.merchantAccount}")
  private String merchantAccount;

  private final UserClient userClient;

  private final PaymentClient paymentClient;

  @Autowired
  public OrdersService (UserClient userClient, PaymentClient paymentClient) {
    this.userClient = userClient;
    this.paymentClient = paymentClient;
  }

  public void processOrder(OrderInput input){
    // gets users info
    // get products info
    // send response to payment service module
    long userID = input.getUserId();
    BigDecimal chargedAmount = input.getPurchases().stream()
        .map(x -> x.getPricePerItem().multiply(new BigDecimal(x.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    PaymentRequest request = new PaymentRequest(input.getCreditCard(), merchantAccount, chargedAmount);
    paymentClient.process(request);
//    userClient.getPaymentDetails(userID);
  }

}
