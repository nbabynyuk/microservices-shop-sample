package com.example.UserApp.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CreditCard {

  public  CreditCard() {}

  public  CreditCard(String cardNumber, String expireAt, String cvcode) {
    this.cardNumber = cardNumber;
    this.expireAt = expireAt;
    this.cvcode = cvcode;
  }

  @Column (unique = true, nullable = false)
  private String cardNumber;

  @Column(nullable = false)
  private String expireAt;

  @Column(nullable = false)
  private String cvcode;

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public void setExpireAt(String expireAt) {
    this.expireAt = expireAt;
  }

  public void setCvcode(String cvcode) {
    this.cvcode = cvcode;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public String getExpireAt() {
    return expireAt;
  }

  public String getCvcode() {
    return cvcode;
  }

}
