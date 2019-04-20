package com.nb.orders.remote;

import com.nb.common.PaymentRequest;
import com.nb.orders.exceptions.PaymentProcessingException;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentClientFallbackFactory implements FallbackFactory<PaymentRequest> {

  private static Logger logger = LoggerFactory.getLogger(PaymentClientFallbackFactory.class);

  @Override
  public PaymentRequest create(Throwable throwable) {
    if(throwable instanceof FeignException) {
      FeignException e = (FeignException) throwable;
      logger.error("error during processing payments, status" + e.status()
          + ", reason:" + e.getMessage());
    }
    throw new PaymentProcessingException(throwable.getMessage(), throwable);
  }
}
