package com.saltyhana.saltyhanaserver.repository;


import java.time.LocalDateTime;
import java.util.Optional;

import com.saltyhana.saltyhanaserver.entity.Icon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.User;


@SpringBootTest
public class GoalRepositoryTest {

  @Autowired
  private GoalRepository goalRepo;
  @Autowired
  private UserRepository userRepo;

  public void testEventListener() {
    Optional<User> user = userRepo.findByEmail("string");
    Goal goal = Goal.builder()
        .name("123")
        .icon(Icon.builder().id(1L).build())
        .category("1233")
        .amount(100L)
        .startAt(LocalDateTime.now())
        .endAt(LocalDateTime.now().plusDays(1))
        .isEnded(false)
        .user(user.get())
        .build();
    goalRepo.save(goal);

    Goal updatedGoal = Goal.builder()
        .id(goal.getId())
        .icon(Icon.builder().id(1L).build())
        .name(goal.getName())
        .category(goal.getCategory())
        .amount(goal.getAmount())
        .startAt(goal.getStartAt())
        .endAt(goal.getEndAt())
        .isEnded(true)
        .user(user.get())
        .build();
    goalRepo.save(updatedGoal);
  }

}
