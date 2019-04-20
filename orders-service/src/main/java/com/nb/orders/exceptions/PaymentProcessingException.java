package com.nb.orders.exceptions;

public class PaymentProcessingException extends RuntimeException {

  public PaymentProcessingException(String cause, Throwable t){
    super(cause, t);
  }

}
