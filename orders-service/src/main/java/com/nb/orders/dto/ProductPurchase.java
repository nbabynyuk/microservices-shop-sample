package com.nb.orders.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductPurchase {

  @NotNull
  private final String productId;

  @NotNull
  private final Integer quantity;

  @NotNull
  private final BigDecimal pricePerItem;

}
