package com.example.UserApp.web;

import com.example.UserApp.errors.ErrorResponse;
import com.example.UserApp.errors.PropertyValueError;
import com.example.UserApp.errors.UserNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlerController {

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
    headers.add("errors", errorResponse.toJSON());
    return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus (HttpStatus.NOT_FOUND)
  public ResponseEntity handleUserNotFoundException() {
    return ResponseEntity.notFound().build();
  }

}
