package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query(value = "SELECT * FROM progress WHERE goal_id = :goalId ORDER BY added_at DESC LIMIT 1", nativeQuery = true)
    Optional<Progress> findRecentProgressByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT p.addedAt FROM Progress p WHERE p.goal.id = :goalId AND p.addedAt BETWEEN :startDate AND :endDate AND p.addedAmount > 0")
    List<LocalDateTime> findAddAtDatesByGoalIdAndDateRange(Long goalId, LocalDateTime startDate, LocalDateTime endDate);

}


