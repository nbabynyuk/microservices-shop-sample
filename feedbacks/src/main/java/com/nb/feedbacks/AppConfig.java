package com.nb.feedbacks;

import com.nb.feedbacks.model.Feedback;
import com.nb.feedbacks.service.FeedbacksCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AppConfig {

    private @Value("${feedbackapp.redis.host}")
    String redisHostname;
    private @Value("${feedbackapp.redis.port}")
    int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHostname, redisPort));
    }

    @Bean
    ReactiveRedisOperations<String, Feedback> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Feedback> serializer = new Jackson2JsonRedisSerializer<>(Feedback.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Feedback> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, Feedback> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    FeedbacksCacheService feedbacksService(ReactiveRedisOperations<String, Feedback> template) {
        return new FeedbacksCacheService(template);
    }
}
