package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionTendencyRepository extends JpaRepository<ConsumptionTendency,Integer> {
}
