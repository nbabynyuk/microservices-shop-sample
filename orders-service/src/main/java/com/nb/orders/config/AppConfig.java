package com.nb.orders.config;

import com.nb.orders.remote.UserClient;
import com.nb.orders.services.OrdersService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public OrdersService ordersServiceFactory(@Autowired UserClient userClient){
    return new OrdersService(userClient);
  }

}
