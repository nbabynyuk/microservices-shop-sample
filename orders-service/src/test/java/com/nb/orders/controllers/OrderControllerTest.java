package com.nb.orders.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.dto.OrderItemInput;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;


  private ObjectMapper mapper = new ObjectMapper();

  private static final String TESTED_PROUDCT_ID = "tested_product";
  private static final long TESTED_USER_ID = 5543L;

  @Test
  public void testCreate() throws Exception {

    OrderItemInput i1 = new OrderItemInput(TESTED_PROUDCT_ID, 1);
    String s = mapper.writeValueAsString(new OrderInput(TESTED_USER_ID, Collections.singleton(i1)));
    mockMvc.perform(
        post("/api/orders")
            .header("Content-Type", "application/json")
            .content(s)
    ).andExpect(status().isCreated());
  }

  @Test
  public void testCreate_invalidDTO() throws Exception {
    String s = mapper.writeValueAsString( new OrderInput(TESTED_USER_ID, Collections.emptyList()));
    mockMvc.perform(
        post("/api/orders")
            .header("Content-Type", "application/json")
            .content(s)
    )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(header().exists("errors"));
  }
}
