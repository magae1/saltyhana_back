package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.saltyhana.saltyhanaserver.entity.RecommendRequest;


public interface RecommendRequestRepository extends
    CrudRepository<RecommendRequest, Long> {

  Optional<RecommendRequest> findByBirthAndDescription(String birth, String description);
}
