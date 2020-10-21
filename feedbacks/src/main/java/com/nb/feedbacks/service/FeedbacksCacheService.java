package com.nb.feedbacks.service;

import com.nb.feedbacks.exceptions.ResourceNotFoundException;
import com.nb.feedbacks.model.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static java.lang.String.format;

public class FeedbacksCacheService {

    public static final Logger logger = LoggerFactory.getLogger(FeedbacksCacheService.class);

    public static final long MAX_RECENT_FEEDBACKS_COUNT = 5;
    private final ReactiveRedisOperations<String, Feedback> redisRepository;

    public FeedbacksCacheService(ReactiveRedisOperations<String, Feedback> redisRepository) {
        this.redisRepository = redisRepository;
    }

    public Mono<Long> create(String productUUID, Feedback feedback) {
        feedback.setCreatedTime(currentTimeAsUtcString());
        feedback.setModifiedTime(currentTimeAsUtcString());
        feedback.setFeedbackUUID(UUID.randomUUID().toString());
        return redisRepository.opsForList().leftPush(productUUID, feedback)
            .map(feedbackCount -> {
                logger.info("feedback message: {}, feedback count: {}", feedback.toString(), feedbackCount);
                if (feedbackCount > MAX_RECENT_FEEDBACKS_COUNT) {
                    return redisRepository
                        .opsForList()
                        .rightPop(productUUID)
                        .map(mostRight -> MAX_RECENT_FEEDBACKS_COUNT);
                } else {
                    return Mono.just(feedbackCount);
                }
            }).flatMap(x -> x);
    }

    public Flux<Feedback> list(String productUUID) {
        return redisRepository
            .opsForList()
            .range(productUUID, 0, MAX_RECENT_FEEDBACKS_COUNT);
    }

    public Mono<Boolean> update(String productUUID, String feedbackUUID, Feedback updatedFeedback) {
        updatedFeedback.setFeedbackUUID(feedbackUUID);
        updatedFeedback.setModifiedTime(currentTimeAsUtcString());
        return redisRepository.opsForList()
            .range(productUUID, 0L, MAX_RECENT_FEEDBACKS_COUNT)
            .collectList()
            .map(items -> {
                Mono<Boolean> returnedResult = findFeedbackAmongAlreadySaved(productUUID,
                    feedbackUUID,
                    updatedFeedback,
                    items);
                if (null == returnedResult)
                    returnedResult = Mono.error(new ResourceNotFoundException(
                        format("feedback with uuid %s not found for product %s", feedbackUUID, productUUID)
                    ));
                return returnedResult;
            }).flatMap(x -> x);
    }

    private Mono<Boolean> findFeedbackAmongAlreadySaved(String productUUID, String feedbackUUID, Feedback updatedFeedback, java.util.List<Feedback> items) {
        Mono<Boolean> returnedResult = null;
        for (int i = 0; i < items.size(); i++) {
            Feedback current = items.get(i);
            if (feedbackUUID.equals(current.getFeedbackUUID())) {
                returnedResult = redisRepository.opsForList().set(productUUID, i, updatedFeedback);
                break;
            }
        }
        return returnedResult;
    }

    public Mono<Long> delete(String productUUID, String feedbackUUID) {
        return redisRepository.opsForList().range(productUUID, 0, MAX_RECENT_FEEDBACKS_COUNT)
            .filter(currentFeedback -> currentFeedback.getFeedbackUUID().equals(feedbackUUID))
            .take(1)
            .single()
            .map(foundFeedback ->
                redisRepository.opsForList().remove(productUUID, 1, foundFeedback))
            .flatMap(x -> x);
    }

    private String currentTimeAsUtcString() {
        return ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
