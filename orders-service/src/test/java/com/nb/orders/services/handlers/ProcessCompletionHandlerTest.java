package com.nb.orders.services.handlers;

import static com.nb.orders.entity.ProcessingStage.PROCESSING_COMPLETE;
import static com.nb.orders.services.OrdersServiceTest.REF_ORDER_UUID;
import static com.nb.orders.services.OrdersServiceTest.REF_PAYMENT_UUID;
import static com.nb.orders.services.OrdersServiceTest.REF_SHIPMENT_UUID;
import static com.nb.orders.services.OrdersServiceTest.REF_USER_ID;
import static com.nb.orders.services.handlers.OrderAcceptedHandlerTest.createOrderRequestForTests;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.ProcessingContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ProcessCompletionHandlerTest {

  @Mock
  private OrdersRepository repository;

  private ProcessCompletionHandler handler;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.handler = new ProcessCompletionHandler(repository);
  }

  @Test
  public void process() {
    OrderRequest r = createOrderRequestForTests();
    ProcessingContext ctx = new ProcessingContext(REF_ORDER_UUID,
        ProcessingStage.PAYMENTS_CONFIRMED,
        REF_SHIPMENT_UUID,
        REF_PAYMENT_UUID);

    when(repository.findById(eq((REF_ORDER_UUID))))
        .thenReturn(Mono.just(new Order(REF_ORDER_UUID, REF_USER_ID, r.getPurchases())));
    when(repository.save(any())).then(invocationOnMock ->
        Mono.just((Order) invocationOnMock.getArguments()[0])
    );

    Mono<ProcessingContext> result = handler.process(r, Mono.just(ctx));
    StepVerifier.create(result).expectNextMatches(c -> c.getNextStage() == PROCESSING_COMPLETE
        && c.getOrder() != null
        && c.getReservationUUID().equals(REF_SHIPMENT_UUID)
        && c.getPaymentUUID().equals(REF_PAYMENT_UUID))
        .verifyComplete();
  }
}