package com.nb.orders.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.nb.common.CreditCardDTO;
import com.nb.common.OperationResult;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.remote.PaymentsRemoteRepository;
import com.nb.orders.remote.StockRemoteRepository;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.handlers.OrderAcceptedHandler;
import com.nb.orders.services.handlers.OrderAcceptedHandlerTest;
import com.nb.orders.services.handlers.PaymentProcessingHandler;
import com.nb.orders.services.handlers.ProcessCompletionHandler;
import com.nb.orders.services.handlers.StockProcessingHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static reactor.core.publisher.Mono.just;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

  public static final long REF_USER_ID = 123L;
  public static final String REF_PRODUCT_ID = "fdsafds";
  public static final String REF_ADDRESS = "Test city";
  public static final CreditCardDTO CREDIT_CARD = new CreditCardDTO("xxx-yyy-zzz",
      "02/19", "111");
  public static final String REF_SHIPMENT_UUID = "333-444-555";
  public static final String REF_ORDER_UUID = "9932-xxx";
  public static final String REF_PAYMENT_UUID = "fwd-234-qqq";

  @Mock
  private PaymentsRemoteRepository paymentsRemoteRepository;

  @Mock
  private StockRemoteRepository stockService;

  @Mock
  private OrdersRepository ordersRepository;

  private OrdersService ordersService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.ordersService = new OrdersService(
        new OrderAcceptedHandler(ordersRepository),
        new StockProcessingHandler(stockService),
        new PaymentProcessingHandler(paymentsRemoteRepository, "123-321"),
        new ProcessCompletionHandler(ordersRepository)
    );
  }

  @Test
  public void testProcessOrder() {
    OrderRequest request = OrderAcceptedHandlerTest.createOrderRequestForTests();

    Mockito.when(ordersRepository.save(any())).then(invocationOnMock -> {
      Order order = (Order) invocationOnMock.getArguments()[0];
      return just(order);
    });
    Mockito.when(paymentsRemoteRepository.process(any()))
        .thenReturn(just(new OperationResult(REF_PAYMENT_UUID)));
    Mockito.when(stockService.processShipmentRequest(any()))
        .thenReturn(Mono.just(new OperationResult(REF_SHIPMENT_UUID)));
    Mockito.when(ordersRepository.findById(anyString()))
        .thenReturn(
            just(new Order(REF_ORDER_UUID, 
                request.getUserId(), 
                request.getPurchases())));
    Mono<Order> result = ordersService.processOrder(request);

    StepVerifier.create(result)
        .expectNextMatches(order -> order.getUuid() != null
            && order.getProcessingStage() == ProcessingStage.PROCESSING_COMPLETE
            && order.getStockReservationId().equals(REF_SHIPMENT_UUID)
            && order.getPaymentId().equals(REF_PAYMENT_UUID))
        .verifyComplete();
  }
}