package com.nb.orders.services;

import com.nb.orders.entity.Order;
import com.nb.orders.entity.ProcessingStage;

public class ProcessingContext {

  private final String orderUuid;
  private ProcessingStage nextStage;
  private String reservationUUID;
  private String paymentUUID;
  private Order order;

  public ProcessingContext(String orderUuid, ProcessingStage nextStage) {
    this.orderUuid = orderUuid;
    this.nextStage = nextStage;
  }

  public ProcessingContext(String orderUuid,
      ProcessingStage nextStage,
      String reservationUUID) {
    this.orderUuid = orderUuid;
    this.nextStage = nextStage;
    this.reservationUUID = reservationUUID;
  }

  public ProcessingContext(ProcessingContext previous,
      ProcessingStage nextStage,
      String paymentUUID) {
    this.orderUuid = previous.getOrderUuid();
    this.reservationUUID = previous.getReservationUUID();
    this.nextStage = nextStage;
    this.paymentUUID = paymentUUID;
  }

  public ProcessingContext(String uuid,
      ProcessingStage nextStage,
      String reservationUUID,
      String paymentUUID) {
    this.orderUuid = uuid;
    this.nextStage = nextStage;
    this.reservationUUID = reservationUUID;
    this.paymentUUID = paymentUUID;
  }

  public ProcessingContext(ProcessingStage s, Order o) {
    this.orderUuid = o.getUuid();
    this.nextStage = s;
    this.reservationUUID = o.getStockReservationId();
    this.paymentUUID = o.getPaymentId();
    this.order = o;
  }

  public String getOrderUuid() {
    return orderUuid;
  }

  public ProcessingStage getNextStage() {
    return nextStage;
  }

  public String getReservationUUID() {
    return reservationUUID;
  }

  public String getPaymentUUID() {
    return paymentUUID;
  }

  public Order getOrder() {
    return this.order;
  }

  @Override
  public String toString() {
    return "ProcessingContext{" +
        "orderUuid='" + orderUuid + '\'' +
        ", nextStage=" + nextStage +
        ", reservationUUID='" + reservationUUID + '\'' +
        ", paymentUUID='" + paymentUUID + '\'' +
        ", order=" + order +
        '}';
  }
}