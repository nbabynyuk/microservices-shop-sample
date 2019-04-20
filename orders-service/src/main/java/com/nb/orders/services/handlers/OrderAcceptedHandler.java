package com.nb.orders.services.handlers;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.ProcessingContext;
import reactor.core.publisher.Mono;

public class OrderAcceptedHandler implements OrderHandler {

  private final OrdersRepository repository;

  public OrderAcceptedHandler(OrdersRepository r) {
    this.repository = r;
  }

  @Override
  public Mono<ProcessingContext> process(OrderRequest request,
      Mono<ProcessingContext> ctxContainer) {
    return ctxContainer.map(ctx ->
        new Order(ctx.getOrderUuid(), request.getUserId(), request.getPurchases()))
        .map(repository::save)
        .flatMap(x -> x)
        .map(o -> new ProcessingContext(o.getUuid(), ProcessingStage.STOCK_RESERVATION));
  }
}
