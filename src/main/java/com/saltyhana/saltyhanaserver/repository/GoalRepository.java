package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user);
    List<Goal> findByUserAndIsEndedFalse(User user);
    List<Goal> findByUserId(Long userId);

    @Query("""
        SELECT g
        FROM Goal g
        LEFT JOIN FETCH g.icon
        WHERE g.user = :user
        AND g.endAt >= CURRENT_DATE
    """)
    List<Goal> findAllByUserWithIcons(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Goal g SET g.isEnded = :isEnded WHERE g.id = :id")
    void updateIsEnded(@Param("id") Long id, @Param("isEnded") boolean isEnded);
}
