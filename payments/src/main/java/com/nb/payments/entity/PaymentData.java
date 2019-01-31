package com.nb.payments.entity;


import com.nb.common.CreditCardDTO;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PaymentData {

  @NotNull
  private String uuid;

  @Valid
  private CreditCardDTO paymentFrom;

  @NotNull
  private String paymentTo;

  @Positive
  private Long amount;

  public PaymentData(CreditCardDTO creditCardDTO, String targetAccount,  Long amount) {
    uuid = UUID.randomUUID().toString();
    this.paymentFrom = creditCardDTO;
    this.paymentTo = targetAccount;
    this.amount = amount;
  }

}
