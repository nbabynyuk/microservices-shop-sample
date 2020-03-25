package com.example.UserApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Data
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

}
