package com.nb.orders.controllers;

import com.nb.common.OperationResult;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.services.OrdersService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {

  private final OrdersService orderService;

  @Autowired
  public OrderController(OrdersService ordersService) {
    this.orderService = ordersService;
  }

  @PostMapping(path = "/api/orders")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<OperationResult> create(@RequestBody @Valid OrderRequest input) {
    return orderService.processOrder(input)
        .map(order -> new OperationResult(order.getUuid()));
  }
}
