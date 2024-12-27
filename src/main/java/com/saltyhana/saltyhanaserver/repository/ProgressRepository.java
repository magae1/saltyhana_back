package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    Optional<Progress> findByGoalId(Long id);
    @Query(value = "SELECT * FROM progress WHERE goal_id = :goalId ORDER BY added_at DESC LIMIT 1", nativeQuery = true)
    Optional<Progress> findRecentProgressByGoalId(@Param("goalId") Long goalId);


}

