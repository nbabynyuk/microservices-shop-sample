package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.PaymentRequest;
import com.nb.orders.services.OrdersServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static com.nb.orders.TestUtils.loadReferenceResourceStub;
import static java.util.Objects.requireNonNull;

class PaymentsRemoteRepositoryTest {

    // the same constants as in resource files
    public static final String REFERENCE_PAYMENTS_GUID = "5342ed13-3f28-40fc-9a93-d7a316c8648f";
    private PaymentsRemoteRepository paymentsRepository;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
            .exchangeFunction(clientRequest -> {
                if ("/api/payments".equals(clientRequest.url().toString())
                    && clientRequest.method().equals(HttpMethod.POST)) {
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(requireNonNull(
                            loadReferenceResourceStub("remote-stubs/payments/create-payment.json")))
                        .build());
                } else {
                    return Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build());
                }
            }).build();
        paymentsRepository = new PaymentsRemoteRepository(webClient);

    }

    @Test
    void whenValidPayloadIsProvidedThenRemoteRepositoryReached() {
        PaymentRequest paymentRequest = new PaymentRequest(
            OrdersServiceTest.CREDIT_CARD,
            "123",
            new BigDecimal(1)
        );
        Mono<OperationResult> futureOperationResult = paymentsRepository.process(paymentRequest);
        StepVerifier.create(futureOperationResult)
            .expectNextMatches(operationResult -> operationResult.getUuid().equals(REFERENCE_PAYMENTS_GUID))
            .verifyComplete();
    }
}