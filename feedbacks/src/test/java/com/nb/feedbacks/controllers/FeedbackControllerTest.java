package com.nb.feedbacks.controllers;

import com.nb.feedbacks.model.Feedback;
import com.nb.feedbacks.service.FeedbacksService;
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
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest({FeedbackController.class})
class FeedbackControllerTest {

    public static final String TEST_PRODUCT_UUID = "da9169fc-65b5-4708-8895-88af85b423e0";
    public static final String TEST_FEEDBACK_UUID = "da9169fc-65b5-4708-8895-88af85b99999";
    public static final String TEST_FEEDBACK_MESSAGE = "feedback #15";

    @MockBean
    private FeedbacksService feedbacksService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void whenCreateEventIsReceivedThenServiceMethodIsCalled() throws IOException {

        String createPayload = loadSampleResource("feedbacks/create.json");

        doNothing().when(feedbacksService).create(eq(TEST_PRODUCT_UUID), argThat(feedback -> {
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
            .when(feedbacksService)
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
        webTestClient
            .delete()
            .uri("/api/products/da9169fc-65b5-4708-8895-88af85b423e0/feedbacks/" + TEST_FEEDBACK_UUID)
            .exchange()
            .expectStatus()
            .isNoContent();

        verify(feedbacksService).delete(eq(TEST_PRODUCT_UUID), eq(TEST_FEEDBACK_UUID));
    }

    private String loadSampleResource(String path) throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
        assert resourceAsStream != null;
        return new String(resourceAsStream.readAllBytes());
    }

    private Feedback createDummyFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackMessage(TEST_FEEDBACK_MESSAGE);
        feedback.setUserId(5L);
        feedback.setUserDisplayName("Test user");
        return feedback;
    }
}