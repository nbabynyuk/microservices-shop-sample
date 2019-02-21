package com.nb.orders.entity;


import com.nb.orders.dto.ProductPurchase;
import java.util.Collection;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Order {

  @Id
  private String uuid;

  @NotEmpty
  private String paymentId;

  @NotEmpty
  private String stockReservationId;

  @NotEmpty
  private Long userId;

  @NotEmpty
  private final Collection<ProductPurchase> purchases;

}
