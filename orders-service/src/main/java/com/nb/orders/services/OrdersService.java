package com.nb.orders.services;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import com.nb.common.ShipmentItem;
import com.nb.common.ShipmentRequest;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.StockClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrdersService {

  @Value("${orders.merchantAccount}")
  private String merchantAccount;

  private final StockClient stockService;

  private final PaymentClient paymentService;

  @Autowired
  public OrdersService (PaymentClient paymentClient, StockClient stockClient) {
    this.paymentService = paymentClient;
    this.stockService = stockClient;
  }

  public void processOrder(OrderInput input){
    prepareShipmentFromStock(input);
    processPayment(input);
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
