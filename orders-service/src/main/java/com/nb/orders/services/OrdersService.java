package com.nb.orders.services;

import com.nb.orders.dto.OrderInput;
import com.nb.orders.remote.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class OrdersService {

  private UserClient userClient;

  @Autowired
  public OrdersService (UserClient userClient) {
    this.userClient = userClient;
  }

  public void processOrder(OrderInput input){
    // gets users info
    // get products info
    // send response to payment service module
    long userID = input.getUserId();
    userClient.getPaymentDetails(userID);
  }

}
