package com.nb.common;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentRequest {

  @NotBlank
  private String address;

  @NotNull
  private Long userId;

  @NotEmpty
  private List<ShipmentItem> products;

  public ShipmentRequest() {}

  public ShipmentRequest(@NotBlank String address,
      @NotNull Long userId,
      @NotEmpty List<ShipmentItem> products) {
    this.address = address;
    this.userId = userId;
    this.products = products;
  }
}
