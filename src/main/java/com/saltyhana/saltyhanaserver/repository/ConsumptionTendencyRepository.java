package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;


@Repository
public interface ConsumptionTendencyRepository extends JpaRepository<ConsumptionTendency, Long> {

    @Query("SELECT t "
        + "FROM ConsumptionTendency t "
        + "WHERE t.score <= :score "
        + "ORDER BY t.score DESC "
        + "LIMIT 1")
    Optional<ConsumptionTendency> findByScore(Integer score);
}
