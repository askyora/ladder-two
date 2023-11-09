package com.yora.ladder.config;



import java.time.Duration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;



@Configuration
@ConditionalOnProperty(prefix = "repository", value = "cache", havingValue = "true",
          matchIfMissing = false)
@EnableCaching
public class CacheConfig {


     @Bean
     public RedisCacheConfiguration cacheConfiguration() {
          return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30))
                    .disableCachingNullValues().serializeValuesWith(SerializationPair
                              .fromSerializer(new GenericJackson2JsonRedisSerializer()));
     }

     @Bean
     public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
          return (builder) -> builder
                    .withCacheConfiguration("ClientCache",
                              RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMinutes(30)))
                    .withCacheConfiguration("StepCache",
                              RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMinutes(30)))
                    .withCacheConfiguration("EntryCache", RedisCacheConfiguration
                              .defaultCacheConfig().entryTtl(Duration.ofMinutes(15)));
     }


}
