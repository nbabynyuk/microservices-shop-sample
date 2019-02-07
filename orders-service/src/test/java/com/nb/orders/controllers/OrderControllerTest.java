package com.nb.orders.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nb.orders.services.OrdersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderControllerTest {

  @Mock
  private OrdersService ordersService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this.getClass());
    this.mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(ordersService))
        .setControllerAdvice(new ErrorHandlerController()).build();
  }

  @Test
  public void testCreate() throws Exception {
    mockMvc.perform(
        post("/api/orders")
            .header("Content-Type", "application/json")
            .content("{ "
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
                + "}")
    ).andExpect(status().isCreated());
  }

  @Test
  public void testCreate_invalidDTO() throws Exception {
    mockMvc.perform(
        post("/api/orders")
            .header("Content-Type", "application/json")
            .content("{\"userId\":5543,\"purchases\":[]}")
    )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(header().exists("errors"));
  }
}
