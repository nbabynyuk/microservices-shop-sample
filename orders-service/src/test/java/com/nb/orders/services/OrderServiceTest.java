package com.nb.orders.services;

import static org.junit.Assert.fail;

import com.nb.common.CreditCardDTO;
import com.nb.orders.dto.OrderInput;
import com.nb.orders.dto.OrderItemInput;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  private final long REF_USER_ID = 123L;
  private final String REF_PRODUCT_ID = "fdsafds";

  @InjectMocks
  private OrdersService ordersService;

  @Test
  public void testProcessOrder(){

    OrderInput input = new OrderInput(REF_USER_ID,
        Collections.singleton(new OrderItemInput(REF_PRODUCT_ID, 1))
    );

    ordersService.processOrder(input);
    fail();

  }

}
