package com.example.UserApp.errors;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

  private String erroCode;
  private String errorMessage;
  private List<PropertyValueError> fieldErrors;

  public ErrorResponse(String erroCode, String errorMessage,
      List<PropertyValueError> fieldErrors) {
    this.erroCode = erroCode;
    this.errorMessage = errorMessage;
    this.fieldErrors = fieldErrors;
  }

  public String getErroCode() {
    return erroCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public List<PropertyValueError> getFieldErrors() {
    return fieldErrors;
  }

  public String toJSON() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    String errorsAsJSON = "{}";
    try {
      errorsAsJSON = mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      //e.printStackTrace();
    }
    return errorsAsJSON;
  }
}
