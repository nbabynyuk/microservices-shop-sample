package com.nb.orders.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nb.common.CreditCardDTO;
import com.nb.common.OperationResult;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.dto.ProductPurchase;
import com.nb.orders.entity.Order;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.StockClient;
import com.nb.orders.repo.OrdersRepository;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  private final long REF_USER_ID = 123L;
  private final String REF_PRODUCT_ID = "fdsafds";

  @Mock
  private PaymentClient paymentClient;

  @Mock
  private StockClient stockService;

  @Mock
  private OrdersRepository ordersRepository;

  private OrdersService ordersService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.ordersService = new OrdersService(paymentClient, stockService, ordersRepository);
  }

  @Test
  public void testProcessOrder(){

    OrderInput input = new OrderInput(REF_USER_ID,
        Collections.singleton(new ProductPurchase(REF_PRODUCT_ID, 2, new BigDecimal(35))),
        new CreditCardDTO("xxx-yyy-zzz", "02/19", "111"),
        "Test city"
    );
    final String stockOperationResult = "xxx";
    final String paymentOperationResult = "yyy";
    final String savedUUID = "1234-4321";

    when(stockService.processShipmentRequest(any())).thenReturn( new OperationResult(stockOperationResult));
    when(paymentClient.process(any())).thenReturn(new OperationResult(paymentOperationResult));
    when(ordersRepository.save( any())).thenReturn(new Mono<Order>() {
      @Override
      public void subscribe(CoreSubscriber<? super Order> coreSubscriber) {
        Order o = new Order(input.getUserId(), input.getPurchases(), stockOperationResult);
        o.setPaymentId(paymentOperationResult);
        o.setUuid(savedUUID);
        coreSubscriber.onNext(o);
      }
    });

   StepVerifier.create(ordersService.processOrder(input))
        .expectNextMatches(order -> order.getUuid().equals(savedUUID))
        .verifyComplete();
  }

}
