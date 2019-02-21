package com.nb.orders.remote;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("StockApp")
public interface StockClient {

  @RequestMapping(method = POST, value = "/api/shipmentRequest", consumes = "application/json")
  OperationResult processShipmentRequest(ShipmentRequest shipmentRequest);

}
