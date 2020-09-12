package com.nb.payments.functional;

import com.nb.common.OperationResult;
import com.nb.payments.entity.PaymentData;
import com.nb.payments.repo.PaymentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

public class PaymentHandlerFunctions {

  private Logger logger = LoggerFactory.getLogger(PaymentHandlerFunctions.class);

  private final PaymentRepo paymentRepo;
  private final Validator validator;

  public PaymentHandlerFunctions(PaymentRepo paymentRepo, Validator v) {
    this.paymentRepo = paymentRepo;
    this.validator = v;
  }

  public Mono<ServerResponse> save(ServerRequest serverRequest) {
   return serverRequest.bodyToMono(PaymentData.class)
       .doOnNext(this::validateRequest)
       .map(paymentData -> save(serverRequest, paymentData))
       .flatMap(x -> x)
       .onErrorResume(this::handleErrors);
  }

  private Mono<ServerResponse> handleErrors(Throwable error) {
    if (error instanceof ValidationViolationException) {
      ValidationViolationException e = (ValidationViolationException) error;
      ErrorResponse resp = new ErrorResponse(e.getMessage(), e.getAdditionalInfo());
      logger.info(" received invalid input: {}", e.toString());
      return ServerResponse.status(HttpStatus.BAD_REQUEST)
          .body(fromValue(resp));
    } else  {
      logger.error("exception happened during processing payment data: {}", error.getMessage());
      ErrorResponse resp = new ErrorResponse(error.getMessage(), Collections.emptyList());
      return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(fromValue(resp));
    }
  }

  private Mono<ServerResponse> save(ServerRequest serverRequest, PaymentData paymentData) {
    String uuid = UUID.randomUUID().toString();
    paymentData.setUuid(uuid);
    Mono<Mono<ServerResponse>> serverResponse = paymentRepo.save(paymentData).map(data -> {
      logger.info(" saved payment data into repository, {}", uuid);
      return ServerResponse.created(serverRequest.uri())
          .body(just(new OperationResult(uuid)), OperationResult.class);
    });
    return serverResponse.flatMap(x -> x);
  }

  private void validateRequest(PaymentData paymentData) {
    Set<ConstraintViolation<PaymentData>> validationContstraints = validator.validate(paymentData);
    if(!validationContstraints.isEmpty()) {
      List<String> additionalInfo = validationContstraints.stream().map(
          constraint -> format("Provided value %s for property %s is invalid. Reason: %s", 
              constraint.getInvalidValue(), constraint.getPropertyPath(), constraint.getMessage()))
          .collect(Collectors.toList());
      throw new ValidationViolationException(additionalInfo);
    }
  }

  private static class ValidationViolationException extends RuntimeException {

    private final List<String> additionalInfo;

    ValidationViolationException(List<String> additionalInfo) {
      super("Received invalid input, most likely that some " +
          "of mandatory fields are missing");
      this.additionalInfo = additionalInfo;
    }

    List<String> getAdditionalInfo() {
      return additionalInfo;
    }

    @Override
    public String toString() {
      return "ValidationViolationException{" +
          "additionalInfo=" + String.join(",", additionalInfo) +
          '}';
    }
  }
}
