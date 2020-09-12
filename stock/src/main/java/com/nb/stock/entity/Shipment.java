package com.nb.stock.entity;

import com.nb.common.ShipmentRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Shipment extends ShipmentRequest {

  private String uuid;

}
