package com.nb.orders.services;

import static com.nb.orders.entity.ProcessingStage.NEW;
import static com.nb.orders.entity.ProcessingStage.PAYMENTS_CONFIRMED;
import static com.nb.orders.entity.ProcessingStage.PAYMENTS_PROCESSING;
import static com.nb.orders.entity.ProcessingStage.STOCK_RESERVATION;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.services.handlers.OrderAcceptedHandler;
import com.nb.orders.services.handlers.OrderHandler;
import com.nb.orders.services.handlers.PaymentProcessingHandler;
import com.nb.orders.services.handlers.ProcessCompletionHandler;
import com.nb.orders.services.handlers.StockProcessingHandler;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OrdersService {

  private Logger logger = LoggerFactory.getLogger(OrdersService.class);

  private Map<ProcessingStage, OrderHandler> handlers = new EnumMap<>(ProcessingStage.class);

  public OrdersService(OrderAcceptedHandler handler,
      StockProcessingHandler stockHandler,
      PaymentProcessingHandler paymentHandler,
      ProcessCompletionHandler completionHandler) {
    handlers.put(NEW, handler);
    handlers.put(STOCK_RESERVATION, stockHandler);
    handlers.put(PAYMENTS_PROCESSING, paymentHandler);
    handlers.put(PAYMENTS_CONFIRMED, completionHandler);

  }

  public Mono<Order> processOrder(OrderRequest request) {
    ProcessingContext ctx = new ProcessingContext(UUID.randomUUID().toString(),
        NEW);
    Mono<ProcessingContext> processingCtx = Mono.just(ctx)
        .publishOn(Schedulers.elastic());
    return processOrderStage(request, processingCtx).map(ProcessingContext::getOrder);
  }

  private Mono<ProcessingContext> processOrderStage(OrderRequest request,
      Mono<ProcessingContext> context) {
    return context.map(ctx -> {
      logger.debug("processing context: {}", ctx.toString());
      if (ProcessingStage.PROCESSING_COMPLETE == ctx.getNextStage()) {
        return context;
      } else {
        OrderHandler h = handlers.get(ctx.getNextStage());
        return processOrderStage(request, h.process(request, context));
      }
    }).flatMap(x -> x);
  }
}