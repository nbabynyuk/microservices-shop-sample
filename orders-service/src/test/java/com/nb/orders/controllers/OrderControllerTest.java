package com.nb.orders.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nb.orders.entity.Order;
import com.nb.orders.services.OrdersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@WebFluxTest({OrderController.class, ErrorHandlerController.class})
public class OrderControllerTest {

  @MockBean
  private OrdersService ordersService;

  @Autowired
  private WebTestClient webTestClient;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this.getClass());
  }

  @Test
  public void testCreate() throws Exception {

    when(ordersService.processOrder(any())).then( x -> {
      Order o = new Order();
      o.setUuid("xxxx");
      return Mono.just(o);
    });

    webTestClient
        .post()
        .uri("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just("{ "
            + "       \"userId\":5543"
            + "       , \"purchases\":["
            + "                {"
            + "                \"productId\":\"tested_product\","
            + "                        \"quantity\":1"
            + "                 }"
            + "           ],"
            + "           \"creditCard\" : {"
            + "             \"cardNumber\": \"xxx-yyy-zz\","
            + "             \"expireAt\" : \"02/19\","
            + "             \"cvcode\": \"111\""
            + "            },"
            + "            \"deliveryAddress\": \"xxxxffdfd, Street 5, 32\""
            + "}"), String.class)
        .exchange()
        .expectStatus().isCreated();

  }

  @Test
  public void testCreate_invalidDTO() throws Exception {
    webTestClient.post()
        .uri("/api/orders")
        .header("Content-Type", "application/json")
        .body( Mono.just("{\"userId\":5543,\"purchases\":[]}"), String.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectHeader().exists("errors");
  }
}
