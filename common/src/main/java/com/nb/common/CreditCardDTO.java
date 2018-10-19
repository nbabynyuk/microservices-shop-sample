package com.nb.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class CreditCardDTO {

  private final String cardNumber;

  private final String expireAt;

  private final String cvcode;

  @JsonCreator
  public CreditCardDTO(@JsonProperty("cardNumber") String cardNumber,
      @JsonProperty("expireAt") String expireAt,
      @JsonProperty("cvcode") String cvcode) {
    this.cardNumber = cardNumber;
    this.expireAt = expireAt;
    this.cvcode = cvcode;
  }

  @NotBlank
  public String getCardNumber() {
    return cardNumber;
  }

  @NotBlank
  public String getExpireAt() {
    return expireAt;
  }

  @NotBlank
  public String getCvcode() {
    return cvcode;
  }
}
