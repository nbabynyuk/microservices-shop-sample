package com.nb.orders.remote;

import com.nb.common.OperationResult;
import com.nb.common.ShipmentItem;
import com.nb.common.ShipmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.nb.orders.TestUtils.loadReferenceResourceStub;
import static com.nb.orders.remote.StockRemoteRepository.DELIVERY_URI;
import static java.util.Objects.requireNonNull;

class StockRemoteRepositoryTest {

    public static final String REFERENCE_PRODUCT_ID = "xxxx-yyy--zzzzz";
    public static final long REFERENCE_USER_ID = 1L;
    public static final int QUANTITY = 3;
    private StockRemoteRepository stockRemoteRepository;

    @BeforeEach
    void setUp() {

        WebClient webClient = WebClient.builder()
            //TODO: implement verification of input parameters,  currently there is no checks what was recievied 
            // as input
            .exchangeFunction(clientRequest -> {
                if (DELIVERY_URI.equals(clientRequest.url().toString())
                    && clientRequest.method().equals(HttpMethod.POST)) {
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("content-type", "application/json")
                        .body(requireNonNull(
                            loadReferenceResourceStub("remote-stubs/delivery/create-delivery.json")))
                        .build());
                } else {
                    return Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build());
                }
            }).build();
        stockRemoteRepository = new StockRemoteRepository(webClient);
    }

    @Test
    void whenShipmentIsPassedThenRemoteServiceIsCalled() {
        ShipmentRequest shipmentRequest = new ShipmentRequest("Test Addresse",
            REFERENCE_USER_ID,
            List.of(new ShipmentItem(REFERENCE_PRODUCT_ID, QUANTITY)));
        Mono<OperationResult> futureResult = stockRemoteRepository.processShipmentRequest(shipmentRequest);
        StepVerifier.create(futureResult)
            .expectNextMatches(operationResult -> operationResult.getUuid().equals("395257ce-96b3-4e18-acae-d2c6aaf75685"))
            .verifyComplete();

    }
}