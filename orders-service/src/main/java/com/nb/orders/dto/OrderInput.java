package com.nb.orders.dto;

import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderInput {

  @NotNull
  private final long userId;

  @NotEmpty
  private final Collection<OrderItemInput> purchases;
}
