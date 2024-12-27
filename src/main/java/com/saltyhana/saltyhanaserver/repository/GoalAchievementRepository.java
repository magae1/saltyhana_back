package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.GoalAchievement;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoalAchievementRepository extends
        CrudRepository<GoalAchievement, Long> {
    Optional<GoalAchievement> findById(Long id);
}
