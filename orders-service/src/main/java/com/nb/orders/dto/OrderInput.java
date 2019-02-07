package com.nb.orders.dto;

import com.nb.common.CreditCardDTO;
import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderInput {

  @NotNull
  private final Long userId;

  @NotEmpty
  private final Collection<ProductPurchase> purchases;

  @NotNull
  private final CreditCardDTO creditCard;

  @NotEmpty
  private final String deliveryAddress;
}
