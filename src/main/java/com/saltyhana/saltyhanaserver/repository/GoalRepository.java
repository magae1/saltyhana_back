package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUser(User user);
    List<Goal> findByUser(User user);
    List<Goal> findByUserAndIsEndedFalse(User user);

    @Query("SELECT g FROM Goal g LEFT JOIN FETCH g.icon WHERE g.user = :user")
    List<Goal> findAllByUserWithIcons(@Param("user") User user);
}
