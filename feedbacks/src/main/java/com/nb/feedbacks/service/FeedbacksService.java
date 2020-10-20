package com.nb.feedbacks.service;

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

public class FeedbacksService {

    public static final Logger logger = LoggerFactory.getLogger(FeedbacksService.class);

    public static final long MAX_RECENT_FEEDBACKS_COUNT = 5;
    private final ReactiveRedisOperations<String, Feedback> redisRepository;

    public FeedbacksService(ReactiveRedisOperations<String, Feedback> redisRepository) {
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

    public void update(String productUUID, Feedback f) {
    }

    public void delete(String productUUID, String feedbackUUID) {
    }

    private String currentTimeAsUtcString() {
        return ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
