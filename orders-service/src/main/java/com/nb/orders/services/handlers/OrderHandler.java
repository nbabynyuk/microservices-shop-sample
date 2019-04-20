package com.nb.orders.services.handlers;

import com.nb.orders.dto.OrderRequest;
import com.nb.orders.services.ProcessingContext;
import reactor.core.publisher.Mono;

public interface OrderHandler {

  Mono<ProcessingContext> process(OrderRequest request, Mono<ProcessingContext> ctx);

}
