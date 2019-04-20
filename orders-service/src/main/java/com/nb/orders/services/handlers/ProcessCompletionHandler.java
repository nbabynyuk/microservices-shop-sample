package com.nb.orders.services.handlers;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.repo.OrdersRepository;
import com.nb.orders.services.ProcessingContext;
import reactor.core.publisher.Mono;

public class ProcessCompletionHandler implements OrderHandler {

  private final OrdersRepository ordersRepository;

  public ProcessCompletionHandler(OrdersRepository r) {
    this.ordersRepository = r;
  }

  @Override
  public Mono<ProcessingContext> process(OrderRequest request,
      Mono<ProcessingContext> ctxContainer) {
    return ctxContainer.map(ctx -> {
      Mono<Order> byId = ordersRepository.findById(ctx.getOrderUuid());
      return byId.map(savedOrder -> {
        savedOrder.updateReservation(ctx.getReservationUUID());
        savedOrder.updatePayments(ctx.getPaymentUUID());
        savedOrder.setProcessingStage(ProcessingStage.PROCESSING_COMPLETE);
        return savedOrder;
      });
    }).flatMap(mono -> mono)
        .map(ordersRepository::save)
        .flatMap(x -> x)
        .map(order -> new ProcessingContext(ProcessingStage.PROCESSING_COMPLETE, order));
  }
}
