package com.nb.stock.controllers;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StockController {

  @PostMapping (value = "/api/shipmentRequest"
      , consumes = MediaType.APPLICATION_JSON_VALUE
      , produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<OperationResult> processShipmentRequest(@Valid @RequestBody ShipmentRequest newShipment) {
       return Mono.just(new OperationResult(UUID.randomUUID().toString()));
  }
}
