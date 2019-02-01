package com.nb.payments.entity;


import com.nb.common.CreditCardDTO;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "payments")
public class PaymentData {

  @Id
  private String uuid;

  @Valid
  @NotNull
  private CreditCardDTO paymentFrom;

  @NotNull
  private String paymentTo;

  @Positive
  @NotNull
  private BigDecimal amount;

}
