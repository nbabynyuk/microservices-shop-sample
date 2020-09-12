package com.nb.orders.services;

import com.nb.orders.remote.PaymentsRemoteRepository;
import com.nb.orders.remote.StockRemoteRepository;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.handlers.OrderAcceptedHandler;
import com.nb.orders.services.handlers.PaymentProcessingHandler;
import com.nb.orders.services.handlers.ProcessCompletionHandler;
import com.nb.orders.services.handlers.StockProcessingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrderConfig {
  
  @Bean
  public OrderAcceptedHandler orderAcceptedHandler(OrdersRepository r) {
    return new OrderAcceptedHandler(r);
  }

  @Bean
  public StockProcessingHandler stockProcessingHandler(StockRemoteRepository stockRemoteRepository) {
    return new StockProcessingHandler(stockRemoteRepository);
  }

  @Bean
  public PaymentProcessingHandler paymentProcessingHandler(PaymentsRemoteRepository client, 
                                                           @Value("orders.MERCHANT_ACCOUNT")String merchantAccount) {
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
  
  @Bean
  public PaymentsRemoteRepository paymentsRemoteRepository(@Value("${orders.PAYMENTS_SERVICE_URI}") String paymentsServiceBaseUri,
                                                           WebClient.Builder webClientBuilder) {
    assert null != paymentsServiceBaseUri;
    WebClient webClient = webClientBuilder.baseUrl(paymentsServiceBaseUri).build();
    return new PaymentsRemoteRepository(webClient);
  }
  
  @Bean
  public StockRemoteRepository stockRemoteRepository(@Value("${orders.STOCK_SERVICE_URI}") String stockServiceUri,
                                                     WebClient.Builder webClientBuilder) {
    assert null != stockServiceUri;
    WebClient webClient = webClientBuilder.baseUrl(stockServiceUri).build();
    return new StockRemoteRepository(webClient);
  }
}
