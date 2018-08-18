package com.example.UserApp.errors;


public class PropertyValueError {

  private String propertyName;
  private String errorCode;
  private String errorMessage;

  public PropertyValueError(String propertyName,
      String errorCode,
      String errorMessage
  ) {
    this.propertyName = propertyName;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
