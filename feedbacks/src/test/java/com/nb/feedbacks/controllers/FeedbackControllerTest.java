package com.nb.feedbacks.controllers;

import com.nb.feedbacks.model.Feedback;
import com.nb.feedbacks.service.FeedbacksCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_FEEDBACK_MESSAGE;
import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_FEEDBACK_UUID;
import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_PRODUCT_UUID;
import static com.nb.feedbacks.FeedbackModuleTestUtil.createDummyFeedback;
import static com.nb.feedbacks.FeedbackModuleTestUtil.loadSampleResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest({FeedbackController.class})
class FeedbackControllerTest {

    @MockBean
    private FeedbacksCacheService feedbacksCacheService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void whenCreateEventIsReceivedThenServiceMethodIsCalled() throws IOException {

        String createPayload = loadSampleResource("feedbacks/create.json");

        doReturn(Mono.just(1L))
            .when(feedbacksCacheService).create(eq(TEST_PRODUCT_UUID), argThat(feedback -> {
            assertEquals(TEST_FEEDBACK_MESSAGE, feedback.getFeedbackMessage());
            assertNotNull(feedback.getFeedbackMessage());
            assertNotNull(feedback.getUserDisplayName());
            assertNotNull(feedback.getUserId());
            return true;
        }));
        webTestClient
            .post()
            .uri("/api/products/da9169fc-65b5-4708-8895-88af85b423e0/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(createPayload), String.class)
            .exchange()
            .expectStatus()
            .isCreated();
    }

    @Test
    public void whenListEventIsReceivedThenServiceMethodIsCalled() {

        doReturn(Flux.fromIterable(
            List.of(createDummyFeedback())))
            .when(feedbacksCacheService)
            .list(eq(TEST_PRODUCT_UUID));
        webTestClient
            .get()
            .uri("/api/products/da9169fc-65b5-4708-8895-88af85b423e0/feedbacks?type=cache")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.[0].feedbackMessage").isEqualTo(TEST_FEEDBACK_MESSAGE);
    }

    @Test
    public void whenDeleteEventIsReceivedThenServiceMethodIsCalled() {
        doReturn(Mono.just(1L))
            .when(feedbacksCacheService)
            .delete(eq(TEST_PRODUCT_UUID), eq(TEST_FEEDBACK_UUID));
        webTestClient
            .delete()
            .uri("/api/products/da9169fc-65b5-4708-8895-88af85b423e0/feedbacks/" + TEST_FEEDBACK_UUID)
            .exchange()
            .expectStatus()
            .isNoContent();
        verify(feedbacksCacheService).delete(eq(TEST_PRODUCT_UUID), eq(TEST_FEEDBACK_UUID));
    }

    @Test
    public void whenEditEventIsReceivedThenServiceMethodIsCalled() throws Exception {

        String payload = loadSampleResource("feedbacks/create.json");

        doReturn(Mono.just(true))
            .when(feedbacksCacheService)
            .update(eq(TEST_PRODUCT_UUID), eq(TEST_FEEDBACK_UUID), any(Feedback.class));

        webTestClient
            .put()
            .uri("/api/products/da9169fc-65b5-4708-8895-88af85b423e0/feedbacks/" + TEST_FEEDBACK_UUID)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(payload), String.class)
            .exchange()
            .expectStatus()
            .isNoContent();

        verify(feedbacksCacheService).update(eq(TEST_PRODUCT_UUID),
            eq(TEST_FEEDBACK_UUID),
            argThat(updatedFeedback ->
                updatedFeedback.getFeedbackMessage().equals("feedback #15")
                    && updatedFeedback.getUserId() == 19L
                    && updatedFeedback.getUserDisplayName().equals("Nazar.B")
            ));
    }
}