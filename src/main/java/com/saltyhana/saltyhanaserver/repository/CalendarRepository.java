package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);
}
