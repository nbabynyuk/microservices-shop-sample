package com.example.user_app.web;

import com.example.user_app.errors.UserNotFoundException;
import com.example.user_app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nb.common.CreditCardDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CreditCardsControllerTest {

  private static final long REF_USER_ID = 5L;

  @Mock
  private UserService userService;

  private MockMvc mockMvc;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(new CreditCardsController(userService))
        .setControllerAdvice(new ErrorHandlerController(new ObjectMapper()))
        .build();
  }

  @Test
  public void getPaymentDetails_success() throws Exception {
    String REF_CREDIT_CARD_NUMBER = "xxx-yyy";
    when(userService.getCreditCards(REF_USER_ID)).thenReturn(Optional.of(Collections.singletonList(
        new CreditCardDTO(REF_CREDIT_CARD_NUMBER, "04/18", "111")
    )));
    this.mockMvc.perform(
        get("/api/users/5/creditCards")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk());
  }

  @Test
  public void getPaymentDetails_user_not_found() throws Exception {
    when(userService.getCreditCards(REF_USER_ID)).thenReturn(Optional.empty());
    this.mockMvc.perform(
        get("/api/users/5/creditCards")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNotFound());
  }

  @Test
  public void addCreditCard_success() throws Exception {
    doNothing().when(userService).updatePaymentMethod(eq(1L), any());
    this.mockMvc.perform(
        put("/api/users/1/creditCards")
            .content("{"
                + "\"operation\":\"ADD\","
                + "\"creditCard\":{"
                + "   \"cardNumber\":\"xxx-yyy\","
                + "   \"expireAt\":\"10/18\","
                + "   \"cvcode\":\"111\""
                + "}}")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk());
  }

  @Test
  public void addCreditCard_nullable_card() throws Exception {
    this.mockMvc.perform(
        put("/api/users/1/creditCards")
            .content("{\"operation\":\"ADD\"}")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest());
  }

  @Test
  public void addCreditCard_invalid_card() throws Exception {
    this.mockMvc.perform(
        put("/api/users/1/creditCards")
            .content("{"
                + "\"operation\":\"ADD\","
                + "\"creditCard\":{"
                + "   \"cardNumber\":\"xxx-yyy\""
                + "   ,\"expireAt\":\"10/18\""
                + "   ,\"cvcode\":null"
                + "}}")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest());
  }

  @Test
  public void addCreditCard_user_not_found() throws Exception {
    doThrow(new UserNotFoundException()).when(userService).updatePaymentMethod(eq(1L), any());

    this.mockMvc.perform(
        put("/api/users/1/creditCards")
            .content("{\"operation\":\"ADD\","
                + "\"creditCard\":{"
                + "    \"cardNumber\":\"xxx-yyy\""
                + "   ,\"expireAt\":\"10/18\""
                + "   ,\"cvcode\":\"111\""
                + "}}")
            .contentType("application/json")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNotFound());
  }
}
