package com.nb.orders.services.handlers;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.services.ProcessingContext;
import java.math.BigDecimal;
import reactor.core.publisher.Mono;

public class PaymentProcessingHandler implements OrderHandler {

  private final PaymentClient paymentClient;

  private final String merchantAccount;

  public PaymentProcessingHandler(PaymentClient paymentClient, String merchantAccount) {
    this.paymentClient = paymentClient;
    this.merchantAccount = merchantAccount;
  }

  @Override
  public Mono<ProcessingContext> process(OrderRequest request,
      Mono<ProcessingContext> ctxContainer) {
    return ctxContainer.map(stageCtx -> {
      BigDecimal chargedAmount = request.getPurchases().stream()
          .map(x -> x.getPricePerItem().multiply(new BigDecimal(x.getQuantity())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      PaymentRequest paymentRequest = new PaymentRequest(request.getCreditCard(),
          merchantAccount,
          chargedAmount);
      OperationResult result = paymentClient.process(paymentRequest);
      return new ProcessingContext(stageCtx, ProcessingStage.PAYMENTS_CONFIRMED, result.getUuid());
    });
  }
}
