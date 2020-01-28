package com.nb.payments.functional;

import java.util.List;

public class ErrorResponse {

  private  String message;
  private List<String> errorDetails;

  ErrorResponse(String message, List<String> errorDetails) {
    this.message = message;
    this.errorDetails = errorDetails;
  }

  public String getMessage() {
    return message;
  }

  public List<String> getErrorDetails() {
    return errorDetails;
  }
}
