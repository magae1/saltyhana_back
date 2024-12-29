package com.saltyhana.saltyhanaserver.component;


import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.saltyhana.saltyhanaserver.dto.GoalMessageDTO;


@Log4j2
@SpringBootTest
public class RedisMessageQueueTest {
  @Autowired
  private RedisMessageQueue<GoalMessageDTO> redisMessageQueue;

  //  @Test
  public void testPushMessage() {
    String keyName = "1";
    redisMessageQueue.pushMessage(keyName, GoalMessageDTO.builder()
        .id(1L)
        .name("123")
        .startAt(LocalDateTime.now())
        .endAt(LocalDateTime.now().plusDays(1))
        .isAchieved(true)
        .build());
  }

  //  @Test
  public void testPopAllMessages() {
    String keyName = "1";
    List<GoalMessageDTO> messages = redisMessageQueue.popAllMessages(keyName);
    log.info(messages);
  }
}
