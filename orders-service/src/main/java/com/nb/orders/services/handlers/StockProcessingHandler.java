package com.nb.orders.services.handlers;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentItem;
import com.nb.common.ShipmentRequest;
import com.nb.orders.dto.OrderRequest;
import com.nb.orders.entity.ProcessingStage;
import com.nb.orders.remote.StockRemoteRepository;
import com.nb.orders.services.ProcessingContext;
import java.util.List;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class StockProcessingHandler implements OrderHandler {

  private final StockRemoteRepository client;

  public StockProcessingHandler(StockRemoteRepository client) {
    this.client = client;
  }

  @Override
  public Mono<ProcessingContext> process(OrderRequest request,
      Mono<ProcessingContext> ctxContainer) {
    return ctxContainer.map(ctx -> {
      List<ShipmentItem> shipmentItems = request.getPurchases().stream()
          .map(pp -> new ShipmentItem(pp.getProductId(), pp.getQuantity()))
          .collect(Collectors.toList());
      ShipmentRequest shipmentRequest = new ShipmentRequest(request.getDeliveryAddress(),
          request.getUserId(),
          shipmentItems);
      OperationResult operationResult = client.processShipmentRequest(shipmentRequest);
      return new ProcessingContext(ctx.getOrderUuid(),
          ProcessingStage.PAYMENTS_PROCESSING,
          operationResult.getUuid());
    });

  }
}
