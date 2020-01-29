package com.example.UserApp.errors;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class ErrorResponse {

  private String errorCode;
  private String errorMessage;
  private List<PropertyValueError> fieldErrors;

  public ErrorResponse(String errorCode, String errorMessage,
      List<PropertyValueError> fieldErrors) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.fieldErrors = fieldErrors;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public List<PropertyValueError> getFieldErrors() {
    return fieldErrors;
  }

}
