package com.nb.orders.services.handlers;

import static com.nb.orders.services.OrdersServiceTest.REF_ADDRESS;
import static com.nb.orders.services.OrdersServiceTest.REF_USER_ID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentRequest;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.remote.StockRemoteRepository;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.ProcessingContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StockProcessingHandlerTest {


  @Mock
  private OrdersRepository repository;

  @Mock
  private StockRemoteRepository stockRemoteRepository;

  private StockProcessingHandler stockProcessingHandler;


  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    stockProcessingHandler = new StockProcessingHandler(stockRemoteRepository);
  }

  @Test
  public void handle() {

    final String orderID = "xxx-yyy-zzz";
    final String reservationUUID = "resevation-123";

    OrderRequest request = OrderAcceptedHandlerTest.createOrderRequestForTests();
    Mono<ProcessingContext> ctx = Mono.just(new ProcessingContext(orderID,
        ProcessingStage.STOCK_RESERVATION));
    when(stockRemoteRepository.processShipmentRequest(argThat(
        new ShipmentRequestMatcher(REF_ADDRESS, REF_USER_ID)
    ))).thenReturn(new OperationResult(reservationUUID));

    Mono<ProcessingContext> processingResult = stockProcessingHandler.process(request, ctx);

    StepVerifier.create(processingResult)
        .expectNextMatches(c -> orderID.equals(c.getOrderUuid())
            && c.getNextStage() == ProcessingStage.PAYMENTS_PROCESSING
            && reservationUUID.equals(c.getReservationUUID()))
        .verifyComplete();
  }

  private static class ShipmentRequestMatcher implements ArgumentMatcher<ShipmentRequest> {

    private final String address;
    private final long userId;

    ShipmentRequestMatcher(String address, long userId) {
      this.address = address;
      this.userId = userId;
    }

    @Override
    public boolean matches(ShipmentRequest shipmentRequest) {
      return userId == shipmentRequest.getUserId()
          && address.equals(shipmentRequest.getAddress());
    }
  }
}