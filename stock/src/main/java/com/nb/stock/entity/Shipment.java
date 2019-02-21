package com.nb.stock.entity;

import com.nb.common.ShipmentRequest;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Shipment extends ShipmentRequest {

  @Id
  private String uuid;

}
