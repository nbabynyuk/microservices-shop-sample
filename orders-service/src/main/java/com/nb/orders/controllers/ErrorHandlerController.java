package com.nb.orders.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ErrorHandlerController {

  @ExceptionHandler({WebExchangeBindException.class})
  public Mono<ResponseEntity<?>> handleError(WebExchangeBindException e){
    List<String> errors = e.getBindingResult().getAllErrors()
        .stream()
        .map(fe ->
            String.format(" fields %s has error: %s ", ((FieldError) fe).getField(),
                fe.getDefaultMessage())
        )
        .collect(Collectors.toList());
    HttpHeaders headers = new HttpHeaders();
    headers.put("errors", errors);
    return Mono.just(new ResponseEntity(headers, HttpStatus.BAD_REQUEST));
  }

}
