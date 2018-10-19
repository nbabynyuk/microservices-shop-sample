package com.nb.orders.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemInput {

  @NotNull
  public final String productId;

  @NotNull
  public final int quantity;

}
