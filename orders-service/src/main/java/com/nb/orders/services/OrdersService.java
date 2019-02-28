package com.nb.orders.services;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import com.nb.common.ShipmentItem;
import com.nb.common.ShipmentRequest;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.entity.Order;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.StockClient;
import com.nb.orders.repo.OrdersRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class OrdersService {

  @Value("${orders.merchantAccount}")
  private String merchantAccount;

  private final StockClient stockService;

  private final PaymentClient paymentService;

  private final OrdersRepository ordersRepository;

  @Autowired
  public OrdersService(PaymentClient paymentClient,
      StockClient stockClient,
      OrdersRepository ordersRepository) {
    this.paymentService = paymentClient;
    this.stockService = stockClient;
    this.ordersRepository = ordersRepository;
  }

  public Mono<Order> processOrder(final OrderInput input){
    return Mono.just(input)
        .publishOn(Schedulers.elastic())
        .map(orderInput -> {
          OperationResult stockReservationResult = prepareShipmentFromStock(input);
          return new Order(input.getUserId(), input.getPurchases(), stockReservationResult.getUuid());
        }).map(order ->  {
          OperationResult paymentResult = processPayment(input);
          order.setPaymentId(paymentResult.getUuid());
          return order;
        }).map(ordersRepository::save)
        .flatMap(savedOrder -> savedOrder);
  }

  private OperationResult processPayment(OrderInput input) {
    BigDecimal chargedAmount = input.getPurchases().stream()
        .map(x -> x.getPricePerItem().multiply(new BigDecimal(x.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    PaymentRequest request = new PaymentRequest(input.getCreditCard(), merchantAccount, chargedAmount);
    return  paymentService.process(request);
  }

  private OperationResult prepareShipmentFromStock(OrderInput input) {
    List<ShipmentItem> shipmentItems = input.getPurchases().stream().map( pp -> new ShipmentItem(pp.getProductId(), pp.getQuantity()))
        .collect(Collectors.toList());
    ShipmentRequest sr = new ShipmentRequest(input.getDeliveryAddress(), input.getUserId(), shipmentItems);
    return stockService.processShipmentRequest(sr);
  }

}
