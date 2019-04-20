package com.nb.orders.services;

import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import org.mockito.ArgumentMatcher;

public class OrderMatcher implements ArgumentMatcher<Order> {

  private final ProcessingStage state;
  private final String uuid;
  private final long userId;

  public OrderMatcher(ProcessingStage state, String uuid, Long userId) {
    this.state = state;
    this.uuid = uuid;
    this.userId = userId;
  }

  @Override
  public boolean matches(Order order) {
    return order.getProcessingStage() == state
        && uuid.equals(order.getUuid())
        && userId == order.getUserId();
  }
}
