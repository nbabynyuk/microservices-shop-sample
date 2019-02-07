package com.nb.orders.controllers;

import com.nb.orders.dto.OrderInput;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class OrderController {

  @RequestMapping(path = "/api/orders", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> create(@RequestBody @Valid OrderInput input) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
