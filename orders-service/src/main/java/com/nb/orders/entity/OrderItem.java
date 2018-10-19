package com.nb.orders.entity;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class OrderItem {

  @Id
  private Long id;

  private Long productId;

  private String productName;

  private Long quantity;

  private BigDecimal pricePerItem;

}
