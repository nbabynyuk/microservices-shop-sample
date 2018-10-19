package com.example.UserApp.web;

import com.example.UserApp.errors.ErrorResponse;
import com.example.UserApp.errors.PropertyValueError;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface DefaultErroHandler {

  default ResponseEntity<?> handleError(BindingResult bindingResult, String errorCode) {
    HttpHeaders headers = new HttpHeaders();
    List<PropertyValueError> fiedlErrors = bindingResult.getAllErrors().stream().map( oe ->
        new PropertyValueError(
            oe.getObjectName(),
            oe.getCode(),
            oe.getDefaultMessage()))
        .collect(Collectors.toList());
    ErrorResponse errorResponse = new ErrorResponse(errorCode,
        "",
        fiedlErrors);
    headers.add("errors", errorResponse.toJSON());
    return  new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
  }

}
