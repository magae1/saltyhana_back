package com.saltyhana.saltyhanaserver.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.saltyhana.saltyhanaserver.dto.GoalMessageDTO;


@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;
  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  @Bean
  public RedisTemplate<String, GoalMessageDTO> redisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, GoalMessageDTO> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(GoalMessageDTO.class));
    return template;
  }
}
