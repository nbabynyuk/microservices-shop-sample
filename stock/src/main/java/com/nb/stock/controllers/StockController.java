package com.nb.stock.controllers;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
public class StockController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping(value = "/", produces = "application/json")
    public Map<String, String> index() {
        return Map.of("name", appName, "version", "1.0");
    }

    @PostMapping (value = "/api/shipmentRequest"
        , consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResult> processShipmentRequest(@Valid @RequestBody ShipmentRequest newShipment) {
        return Mono.just(new OperationResult(UUID.randomUUID().toString()));
    }
}
