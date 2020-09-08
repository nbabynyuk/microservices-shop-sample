package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;

import java.util.UUID;

public class StockRemoteRepository {

  public OperationResult processShipmentRequest(ShipmentRequest shipmentRequest) {
    return new OperationResult(UUID.randomUUID().toString());
  }

}
