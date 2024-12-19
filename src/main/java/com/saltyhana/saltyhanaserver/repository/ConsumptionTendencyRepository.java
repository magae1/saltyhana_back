package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionTendencyRepository extends JpaRepository<ConsumptionTendency, Long> {
    @Query("SELECT DISTRICT(t) FROM ConsumptionTendency t WHERE t.score <= :score")
    Optional<ConsumptionTendency> findByScore(Integer score);
}
