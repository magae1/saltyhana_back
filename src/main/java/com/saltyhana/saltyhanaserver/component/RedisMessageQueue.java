package com.saltyhana.saltyhanaserver.component;


import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;


/**
 * Redis ListQueue를 이용한 서비스입니다. Redis와 연결이 반드시 필요합니다.
 */
@Component
public class RedisMessageQueue<T> {

  @Value("${msg_queue_key_prefix}")
  private String keyPrefix;

  @Resource(name = "redisTemplate")
  private ListOperations<String, T> listOperations;


  /**
   * keyName에 해당하는 리스트에 메시지를 추가합니다.
   *
   * @param keyName 리스트의 키 이름입니다. 주로 userId를 사용합니다.
   * @param message 추가할 메시지입니다.
   */
  public void pushMessage(String keyName, T message) {
    listOperations.rightPush(keyPrefix + keyName, message);
  }

  /**
   * keyName에 해당하는 모든 메시지들을 반환하고, 삭제합니다(pop).
   *
   * @param keyName 리스트의 키 이름입니다. 주로 userId를 사용합니다.
   * @return keyName에 해당하는 모든 메시지들을 반환합니다. 메시지가 없다면 빈 리스트일 수 있습니다.
   */
  public List<T> popAllMessages(String keyName) {
    List<T> list = new ArrayList<>();
    while (true) {
      T message = popMessage(keyName);
      if (message == null) {
        break;
      }
      list.add(message);
    }
    return list;
  }

  private T popMessage(String keyName) {
    return listOperations.leftPop(keyPrefix + keyName);
  }
}
