package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findFirstByGoalIdOrderByAfterAmountDesc(Long goalId);

    @Query("SELECT p.afterAmount FROM Progress p " +
            "WHERE p.addedAt = (SELECT MAX(p2.addedAt) " +
            "FROM Progress p2 " +
            "WHERE p2.goal.id = :goalId) " +
            "AND p.goal.id = :goalId")
    Long findLatestAfterAmountByGoalId(@Param("goalId") Long goalId);

}

