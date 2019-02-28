package com.nb.orders.entity;


import com.nb.orders.dto.ProductPurchase;
import java.util.Collection;
import java.util.Collections;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
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
  private Collection<ProductPurchase> purchases;

  public Order() {}

  public Order(Long userId, Collection< ProductPurchase> productPurchases, String stockReservationId) {
    this.userId = userId;
    this.purchases = Collections.unmodifiableCollection(productPurchases);
    this.stockReservationId = stockReservationId;
  }

}
