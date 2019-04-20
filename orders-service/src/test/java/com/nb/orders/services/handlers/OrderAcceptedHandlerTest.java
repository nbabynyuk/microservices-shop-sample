package com.nb.orders.services.handlers;

import static com.nb.orders.services.OrdersServiceTest.REF_ADDRESS;
import static com.nb.orders.services.OrdersServiceTest.REF_PRODUCT_ID;
import static com.nb.orders.services.OrdersServiceTest.REF_USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.dto.ProductPurchase;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.OrderMatcher;
import com.nb.orders.services.OrdersServiceTest;
import com.nb.orders.services.ProcessingContext;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class OrderAcceptedHandlerTest {

  @Mock
  private OrdersRepository repository;

  private OrderAcceptedHandler handler;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.handler = new OrderAcceptedHandler(repository);
  }

  @Test
  public void test() {
    final String refUUID = "111-222-333";
    OrderRequest input = createOrderRequestForTests();
    when(repository.save(argThat(new OrderMatcher(ProcessingStage.NEW, refUUID, REF_USER_ID))))
        .then((Answer<Mono<Order>>) invocationOnMock -> {
          Order o = invocationOnMock.getArgument(0);
          return Mono.just(o);
        });
    Mono<ProcessingContext> processingCtx = Mono
        .just(new ProcessingContext(refUUID, ProcessingStage.NEW));
    Mono<ProcessingContext> result = handler.process(input, processingCtx);
    StepVerifier.create(result)
        .expectNextMatches(ctx ->
            ctx.getNextStage() == ProcessingStage.STOCK_RESERVATION
                && ctx.getOrderUuid().equals(refUUID)
        )
        .verifyComplete();
    verify(repository).save(any(Order.class));

  }

  public static OrderRequest createOrderRequestForTests() {
    return new OrderRequest(REF_USER_ID,
        Collections.singleton(new ProductPurchase(REF_PRODUCT_ID, 2, new BigDecimal(35))),
        OrdersServiceTest.CREDIT_CARD,
        REF_ADDRESS
    );
  }


}