package com.nb.orders.services;

import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.StockClient;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.handlers.OrderAcceptedHandler;
import com.nb.orders.services.handlers.PaymentProcessingHandler;
import com.nb.orders.services.handlers.ProcessCompletionHandler;
import com.nb.orders.services.handlers.StockProcessingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

  @Value("${orders.merchantAccount}")
  private String merchantAccount;

  @Bean
  public OrderAcceptedHandler orderAcceptedHandler(OrdersRepository r) {
    return new OrderAcceptedHandler(r);
  }

  @Bean
  public StockProcessingHandler stockProcessingHandler(StockClient stockClient) {
    return new StockProcessingHandler(stockClient);
  }

  @Bean
  public PaymentProcessingHandler paymentProcessingHandler(PaymentClient client) {
    return new PaymentProcessingHandler(client, merchantAccount);
  }

  @Bean
  public ProcessCompletionHandler processCompletionHandler(OrdersRepository r) {
    return new ProcessCompletionHandler(r);
  }

  @Bean
  public OrdersService ordersService(OrderAcceptedHandler handler,
      StockProcessingHandler stockHandler,
      PaymentProcessingHandler paymentHandler,
      ProcessCompletionHandler completionHandler) {
    return new OrdersService(handler, stockHandler, paymentHandler, completionHandler);
  }
}
