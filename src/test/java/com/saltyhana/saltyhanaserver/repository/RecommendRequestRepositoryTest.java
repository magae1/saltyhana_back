package com.saltyhana.saltyhanaserver.repository;

import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.saltyhana.saltyhanaserver.entity.RecommendRequest;


@Log4j2
@SpringBootTest
public class RecommendRequestRepositoryTest {

  @Autowired
  private RecommendRequestRepository recommendRequestRepository;

  public void testInsert() {
    RecommendRequest recommendRequest = RecommendRequest.builder()
        .birth("2024-12-01")
        .description("안녕하세요")
        .response("반갑습니다.")
        .build();

    recommendRequestRepository.save(recommendRequest);
  }

  public void testFindByBirthAndDescription() {
    Optional<RecommendRequest> result = recommendRequestRepository.findByBirthAndDescription(
        "2024-12-01", "안녕하세요");

    result.ifPresent(recommendRequest -> log.info(recommendRequest.toString()));

  }

}
