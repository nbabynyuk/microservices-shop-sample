package com.example.user_app.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nb.common.CreditCardDTO;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PaymentMethodUpdateRequests {

  private final CreditCardOperations operation;

  private final CreditCardDTO creditCard;

  @JsonCreator
  public PaymentMethodUpdateRequests(
      @JsonProperty("operation") CreditCardOperations operation,
      @JsonProperty("creditCard") CreditCardDTO creditCard) {
    this.operation = operation;
    this.creditCard = creditCard;
  }

  @NotNull
  public CreditCardOperations getOperation() {
    return operation;
  }

  @NotNull
  @Valid
  public CreditCardDTO getCreditCard() {
    return creditCard;
  }
}
