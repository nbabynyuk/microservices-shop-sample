package com.nb.common;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShipmentItem {

  @NotEmpty
  private String productId;

  @NotNull
  private Integer quantity;

  public ShipmentItem(@NotEmpty String productId,
      @NotNull Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }
}
