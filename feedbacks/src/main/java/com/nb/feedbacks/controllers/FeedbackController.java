package com.nb.feedbacks.controllers;

import com.nb.feedbacks.model.Feedback;
import com.nb.feedbacks.service.FeedbacksCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/products/{productUUID}/feedbacks")
public class FeedbackController {
    public static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    public static final long MAX_RECENT_FEEDBACKS_COUNT = 5;
    public static final String CACHE_QUERY_TYPE = "cache";
    public static final String ALL_QUERY_TYPE = "all";

    private final FeedbacksCacheService feedbacksCacheService;

    @Autowired
    public FeedbackController(FeedbacksCacheService feedbacksCacheService) {
        this.feedbacksCacheService = feedbacksCacheService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmptyResponse> createFeedBack(@PathVariable("productUUID") String productUUID,
                                              @NotNull @RequestBody Feedback feedback) {
        return feedbacksCacheService.create(productUUID, feedback)
            .map(feedbacksCount -> new EmptyResponse());
    }

    @GetMapping
    public Flux<Feedback> listFeedBacks(@PathVariable String productUUID,
                                        @RequestParam(required = false, defaultValue = CACHE_QUERY_TYPE) String type) {
        if (CACHE_QUERY_TYPE.equals(type)) {
            return feedbacksCacheService.list(productUUID);
        } else {
            throw new IllegalArgumentException("Not yet supported");
        }
    }

    @DeleteMapping("/{feedbackUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<EmptyResponse> deleteFeedBack(@PathVariable String productUUID,
                                              @PathVariable String feedbackUUID) {
        return feedbacksCacheService.delete(productUUID, feedbackUUID)
            .map(removedItemsCount -> new EmptyResponse());
    }

    @PutMapping("/{feedbackUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<EmptyResponse> deleteFeedBack(@PathVariable String productUUID,
                                              @PathVariable String feedbackUUID,
                                              @NotNull @RequestBody Feedback feedback) {
        return feedbacksCacheService.update(productUUID, feedbackUUID, feedback)
            .map(removedItemsCount -> new EmptyResponse());
    }

}
