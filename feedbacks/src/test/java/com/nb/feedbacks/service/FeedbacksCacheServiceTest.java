package com.nb.feedbacks.service;

import com.nb.feedbacks.model.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_FEEDBACK_MESSAGE;
import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_FEEDBACK_UUID;
import static com.nb.feedbacks.FeedbackModuleTestUtil.TEST_PRODUCT_UUID;
import static com.nb.feedbacks.FeedbackModuleTestUtil.createDummyFeedback;
import static com.nb.feedbacks.FeedbackModuleTestUtil.dummyFeedbacksSource;
import static com.nb.feedbacks.service.FeedbacksCacheService.MAX_RECENT_FEEDBACKS_COUNT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbacksCacheServiceTest {

    @Mock
    private ReactiveRedisOperations<String, Feedback> redisRepository;

    @Mock
    private ReactiveListOperations<String, Feedback> reactiveListOperations;

    private FeedbacksCacheService feedbacksCacheService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
        this.feedbacksCacheService = new FeedbacksCacheService(redisRepository);
    }

    @Test
    void whenCreateEventIsReceivedAndThereIsLessThenThresholdRecordsThenRepoIsCalled() {

        ArgumentCaptor<Feedback> feedbackArgumentCaptor = ArgumentCaptor.forClass(Feedback.class);

        when(reactiveListOperations.leftPush(eq(TEST_PRODUCT_UUID), feedbackArgumentCaptor.capture()))
            .thenReturn(Mono.just(1L));
        doReturn(reactiveListOperations).when(redisRepository).opsForList();
        Mono<Long> createResult = feedbacksCacheService.create(TEST_PRODUCT_UUID, createDummyFeedback());
        StepVerifier.create(createResult)
            .expectNext(1L)
            .verifyComplete();

        verify(reactiveListOperations).leftPush(eq(TEST_PRODUCT_UUID), any());
        verifyCapturedFeedback(feedbackArgumentCaptor.getValue());
    }

    @Test
    void whenCreateEventIsReceivedAndThereIsGreaterThenThresholdRecordsThenRepoIsCalled() {
        ArgumentCaptor<Feedback> feedbackArgumentCaptor = ArgumentCaptor.forClass(Feedback.class);

        when(reactiveListOperations.leftPush(eq(TEST_PRODUCT_UUID), feedbackArgumentCaptor.capture()))
            .thenReturn(Mono.just(6L));
        when(reactiveListOperations.rightPop(eq(TEST_PRODUCT_UUID)))
            .thenReturn(Mono.just(new Feedback()));
        doReturn(reactiveListOperations).when(redisRepository).opsForList();
        Mono<Long> createResult
            = feedbacksCacheService.create(TEST_PRODUCT_UUID, createDummyFeedback());
        createResult.subscribe();

        verify(reactiveListOperations).leftPush(eq(TEST_PRODUCT_UUID), any());
        verify(reactiveListOperations).rightPop(eq(TEST_PRODUCT_UUID));
        verifyCapturedFeedback(feedbackArgumentCaptor.getValue());
    }

    private void verifyCapturedFeedback(Feedback capturedFeedback) {
        assertNotNull(capturedFeedback.getCreatedTime());
        assertNotNull(capturedFeedback.getModifiedTime());
        assertNotNull(capturedFeedback.getFeedbackUUID());
    }

    @Test
    public void whenListEventIsReceivedThenServiceIsCalled() {
        doReturn(Flux.fromIterable(List.of(createDummyFeedback())))
            .when(reactiveListOperations)
            .range(eq(TEST_PRODUCT_UUID), eq(0L), eq(MAX_RECENT_FEEDBACKS_COUNT));
        doReturn(reactiveListOperations).when(redisRepository).opsForList();
        Flux<Feedback> list = feedbacksCacheService.list(TEST_PRODUCT_UUID);
        StepVerifier.create(list)
            .expectSubscription()
            .expectNextMatches(f -> {
                //all other verification is here
                return f.getFeedbackMessage().equals(TEST_FEEDBACK_MESSAGE);
            }).verifyComplete();
    }

    @Test
    public void whenDeleteEventIsReceivedThenServiceIsCalled() {
        doReturn(dummyFeedbacksSource())
            .when(reactiveListOperations)
            .range(eq(TEST_PRODUCT_UUID),
                eq(0L),
                eq(MAX_RECENT_FEEDBACKS_COUNT));
        doReturn(Mono.just(1L))
            .when(reactiveListOperations)
            .remove(eq(TEST_PRODUCT_UUID), eq(1L), any(Feedback.class));
        doReturn(reactiveListOperations).when(redisRepository).opsForList();
        Mono<Long> delete = feedbacksCacheService.delete(TEST_PRODUCT_UUID, TEST_FEEDBACK_UUID);
        StepVerifier.create(delete)
            .expectNext(1L)
            .verifyComplete();
    }

    @Test
    public void whenEditEventIsCalledThenRepositoryIsCalled() {
        doReturn(dummyFeedbacksSource())
            .when(reactiveListOperations)
            .range(eq(TEST_PRODUCT_UUID),
                eq(0L),
                eq(MAX_RECENT_FEEDBACKS_COUNT));
        doReturn(Mono.just(true))
            .when(reactiveListOperations)
            .set(eq(TEST_PRODUCT_UUID),
                eq(0L),
                argThat(updatedFeedback ->
                    updatedFeedback.getFeedbackUUID().equals(TEST_FEEDBACK_UUID)
                        && updatedFeedback.getModifiedTime() != null));

        doReturn(reactiveListOperations).when(redisRepository).opsForList();
        Feedback dummyFeedback = createDummyFeedback();
        dummyFeedback.setFeedbackMessage("edited msg");
        Mono<Boolean> updateResult = feedbacksCacheService.update(TEST_PRODUCT_UUID, TEST_FEEDBACK_UUID, dummyFeedback);
        StepVerifier.create(updateResult)
            .expectNext(true)
            .verifyComplete();
    }
}