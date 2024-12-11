package com.saltyhana.saltyhanaserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saltyhana.saltyhanaserver.entity.Sample;


public interface SampleRepository extends JpaRepository<Sample, Long> {
}
