package com.nb.orders.entity;

public enum ProcessingStage {
  NEW,
  STOCK_RESERVATION,
  PAYMENTS_PROCESSING,
  PAYMENTS_CONFIRMED,
  PROCESSING_COMPLETE
}
