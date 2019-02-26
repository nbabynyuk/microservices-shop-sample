package com.nb.orders.controllers;

import com.nb.common.OperationResult;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.services.OrdersService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {

  private final OrdersService orderService;

  @Autowired
  public OrderController(OrdersService ordersService) {
    this.orderService = ordersService;
  }

  @RequestMapping(path = "/api/orders", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<OperationResult> create(@RequestBody @Valid OrderInput input) {
    return orderService.processOrder(input)
        .map(order -> new OperationResult(order.getUuid()));
  }
}
