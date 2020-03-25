package com.nb.products.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;

@Data
class Product {

  private String id;

  @NotNull
  private String name;

  @NotNull
  private String description;

  private Collection<String> mediaFiles;

  @NotNull
  private BigDecimal currentPrice;

}
