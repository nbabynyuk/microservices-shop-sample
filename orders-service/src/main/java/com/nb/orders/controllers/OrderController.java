package com.nb.orders.controllers;

import com.nb.orders.dto.OrderInput;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class OrderController {

  @RequestMapping(path = "/api/orders", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> create(@RequestBody @Valid OrderInput input,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors() || (input == null)) {
      List<String> errors = bindingResult.getAllErrors()
          .stream()
          .map(fe ->
              String.format(" fields %s has error: %s ", ((FieldError) fe).getField(),
                  fe.getDefaultMessage())
          )
          .collect(Collectors.toList());
      HttpHeaders headers = new HttpHeaders();
      headers.put("errors", errors);
      return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
  }


}
