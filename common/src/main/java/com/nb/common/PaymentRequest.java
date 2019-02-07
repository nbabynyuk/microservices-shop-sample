package com.nb.common;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

  @Valid
  @NotNull
  private CreditCardDTO paymentFrom;

  @NotNull
  private String paymentTo;

  @Positive
  @NotNull
  private BigDecimal amount;

  public PaymentRequest() {}

  public PaymentRequest(CreditCardDTO creditCard, String merchantAccount, BigDecimal chargedAmount) {
    this.paymentFrom = creditCard;
    this.paymentTo = merchantAccount;
    this.amount = chargedAmount;
  }
}
