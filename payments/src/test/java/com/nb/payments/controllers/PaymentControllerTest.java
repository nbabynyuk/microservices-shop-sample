package com.nb.payments.controllers;

import static org.junit.Assert.assertTrue;

import com.nb.common.CreditCardDTO;
import com.nb.payments.entity.PaymentData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@WebFluxTest(PaymentController.class)
public class PaymentControllerTest {

  @Autowired
  private WebTestClient webTestClient;


  @Test
  public void testProcessPayment() throws Exception {


    PaymentData pd = new PaymentData(
        new CreditCardDTO("xxxx-yyyy-zzzz", "02/19", "111"),
        "999-777-0",
        500L );

    assertTrue(true);



  }

}
