package com.saltyhana.saltyhanaserver.listener;


import com.saltyhana.saltyhanaserver.dto.GoalMessageDTO;
import com.saltyhana.saltyhanaserver.entity.Icon;
import jakarta.persistence.PostUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.saltyhana.saltyhanaserver.service.GoalMessageQueueService;
import com.saltyhana.saltyhanaserver.entity.Goal;


@Component
@Log4j2
public class GoalEntityListener {

  @Autowired
  private GoalMessageQueueService goalMessageQueueService;

  @PostUpdate
  public void onPostUpdate(Goal goal) {
    boolean isEnded = goal.isEnded();
    if (!isEnded) {
      return;
    }
    log.info("목표 {}이 완료됐습니다.", goal.getId());
    String iconUrl = goal.getCustomImage();
    if (iconUrl != null) {
      Icon icon = goal.getIcon();
      iconUrl = icon.getIconImage();
    }
    GoalMessageDTO goalMessageDTO = GoalMessageDTO.builder()
        .id(goal.getId())
        .name(goal.getName())
        .iconUrl(iconUrl)
        .category(goal.getCategory())
        .amount(goal.getAmount())
        .isAchieved(true)
        .startAt(goal.getStartAt())
        .endAt(goal.getEndAt())
        .build();
    String keyName = Long.toString(goal.getUser().getId());
    log.debug("key: {}, DTO: {}", keyName, goalMessageDTO);
    goalMessageQueueService.pushMessage(keyName, goalMessageDTO);
  }
}
