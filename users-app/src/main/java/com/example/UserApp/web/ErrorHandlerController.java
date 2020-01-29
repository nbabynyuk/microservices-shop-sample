package com.example.UserApp.web;

import com.example.UserApp.errors.ErrorResponse;
import com.example.UserApp.errors.PasswordMismatchException;
import com.example.UserApp.errors.PropertyValueError;
import com.example.UserApp.errors.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlerController {

  private Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

  private ObjectMapper mapper = new ObjectMapper();

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity handleError(MethodArgumentNotValidException e) {
    HttpHeaders headers = new HttpHeaders();
    List<PropertyValueError> fieldErrors = e.getBindingResult().getAllErrors().stream().map( oe ->
        new PropertyValueError(
            oe.getObjectName(),
            oe.getCode(),
            oe.getDefaultMessage()))
        .collect(Collectors.toList());
    ErrorResponse errorResponse = new ErrorResponse("validation_failed",
        "",
        fieldErrors);
    headers.add("errors", convertToString(errorResponse));
    return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus (HttpStatus.NOT_FOUND)
  public ResponseEntity handleUserNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(PasswordMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handlePasswordMismatch() {
    ErrorResponse r = new ErrorResponse("password_confirmation_mismatch",
        "Password confirmation doesn't equal to password",
        Collections.emptyList());
    return ResponseEntity.badRequest().header("errors", convertToString(r)).build();
  }

  private String convertToString(ErrorResponse response){
    try {
      return mapper.writeValueAsString(response);
    } catch (JsonProcessingException e) {
      logger.info("exception happened during conversion to json", e);
      return "";
    }
  }
}
