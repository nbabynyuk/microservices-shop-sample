package com.nb.orders.services.handlers;

import static com.nb.orders.entity.ProcessingStage.PAYMENTS_CONFIRMED;
import static com.nb.orders.entity.ProcessingStage.PAYMENTS_PROCESSING;
import static com.nb.orders.services.OrdersServiceTest.CREDIT_CARD;
import static com.nb.orders.services.OrdersServiceTest.REF_ORDER_UUID;
import static com.nb.orders.services.OrdersServiceTest.REF_PAYMENT_UUID;
import static com.nb.orders.services.OrdersServiceTest.REF_SHIPMENT_UUID;
import static com.nb.orders.services.handlers.OrderAcceptedHandlerTest.createOrderRequestForTests;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.nb.common.CreditCardDTO;
import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.services.ProcessingContext;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class PaymentProcessingHandlerTest {

  @Mock
  private PaymentClient client;

  private PaymentProcessingHandler paymentHandler;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    paymentHandler = new PaymentProcessingHandler(client, "1234-3456");
  }

  @Test
  public void process() {
    OrderRequest request = createOrderRequestForTests();

    ProcessingContext ctx = new ProcessingContext(REF_ORDER_UUID,
        PAYMENTS_PROCESSING, REF_SHIPMENT_UUID);
    Mono<ProcessingContext> ctxContainer = Mono.just(ctx);
    when(client.process(
        argThat(
            new PaymentRequestMatcher(CREDIT_CARD, new BigDecimal(70)))))
        .thenReturn(new OperationResult(REF_PAYMENT_UUID));
    Mono<ProcessingContext> result = paymentHandler.process(request, ctxContainer);
    StepVerifier.create(result)
        .expectNextMatches(c -> c.getNextStage() == PAYMENTS_CONFIRMED
            && REF_PAYMENT_UUID.equals(c.getPaymentUUID()))
        .verifyComplete();
  }

  private static class PaymentRequestMatcher implements ArgumentMatcher<PaymentRequest> {

    final CreditCardDTO creditCard;
    final BigDecimal orderSum;

    PaymentRequestMatcher(CreditCardDTO creditCard, BigDecimal orderAmount) {
      this.creditCard = creditCard;
      this.orderSum = orderAmount;
    }

    @Override
    public boolean matches(PaymentRequest request) {
      return this.creditCard.equals(request.getPaymentFrom())
          && orderSum.equals(request.getAmount());
    }
  }
}