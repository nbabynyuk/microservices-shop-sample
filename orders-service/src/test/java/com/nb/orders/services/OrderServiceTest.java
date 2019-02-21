package com.nb.orders.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.nb.common.CreditCardDTO;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.dto.ProductPurchase;
import com.nb.orders.remote.PaymentClient;
import com.nb.orders.remote.StockClient;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  private final long REF_USER_ID = 123L;
  private final String REF_PRODUCT_ID = "fdsafds";

  @Mock
  private PaymentClient paymentClient;

  @Mock
  private StockClient stockService;

  private OrdersService ordersService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.ordersService = new OrdersService(paymentClient, stockService);
  }

  @Test
  public void testProcessOrder(){

    OrderInput input = new OrderInput(REF_USER_ID,
        Collections.singleton(new ProductPurchase(REF_PRODUCT_ID, 2, new BigDecimal(35))),
        new CreditCardDTO("xxx-yyy-zzz", "02/19", "111"),
        "Test city"
    );
    ordersService.processOrder(input);
    verify(paymentClient).process(any());
    verify(stockService).processShipmentRequest(any());
  }

}
