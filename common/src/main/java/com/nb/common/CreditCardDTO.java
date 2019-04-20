package com.nb.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditCardDTO that = (CreditCardDTO) o;
    return cardNumber.equals(that.cardNumber) &&
        expireAt.equals(that.expireAt) &&
        cvcode.equals(that.cvcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cardNumber, expireAt, cvcode);
  }
}
