package com.nb.payments;

import com.nb.payments.functional.PaymentHandlerFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AppRoutingConfig {

  @Autowired
  private PaymentHandlerFunctions paymentHandler;

  @Bean
  RouterFunction<ServerResponse> routing(){
    Map<String, String> index = Map.of("name", "paymentStub",
        "version", "1.0.0");
    return route(GET("/"), serverRequest -> ok().body(fromValue(index)))
            .andRoute(POST("/api/payments"), paymentHandler::save);
  }
}