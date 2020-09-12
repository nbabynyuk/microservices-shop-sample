package com.nb.payments.entity;


import com.nb.common.PaymentRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "payments")
public class PaymentData extends PaymentRequest {

  @Id
  private String uuid;

}
