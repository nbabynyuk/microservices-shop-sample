package com.nb.products.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Product {

  @Id
  private String id;

  @NotNull
  private String name;

  @NotNull
  private String description;

  private Collection<String> mediaFiles;

  @NotNull
  private BigDecimal currentPrice;

}
