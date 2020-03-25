package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("StockApp")
public interface StockClient {

  @PostMapping(value = "/api/shipmentRequest", consumes = "application/json")
  OperationResult processShipmentRequest(ShipmentRequest shipmentRequest);

}
