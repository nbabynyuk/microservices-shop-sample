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

  private String paymentId;

  private String stockReservationId;

  @NotEmpty
  private Long userId;

  @NotEmpty
  private Collection<ProductPurchase> purchases;

  @NotEmpty
  private ProcessingStage processingStage;

  public Order() {}

  public Order(String uuid, Long userId, Collection< ProductPurchase> productPurchases) {
    this.uuid = uuid;
    this.userId = userId;
    this.purchases = Collections.unmodifiableCollection(productPurchases);
    processingStage = ProcessingStage.NEW;
  }

  public void updateReservation(String reservationID) {
    this.stockReservationId = reservationID;
  }

  public void updatePayments(String paymentsID) {
    this.paymentId = paymentsID;
  }

  public void setProcessingStage(ProcessingStage processingStage) {
    this.processingStage = processingStage;
  }

}
