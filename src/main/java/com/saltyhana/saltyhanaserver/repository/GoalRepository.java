package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUser(User user);
    List<Goal> findByUser(User user);
    List<Goal> findByUserAndIsEndedFalse(User user);
}
