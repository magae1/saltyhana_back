package com.saltyhana.saltyhanaserver.repository;


import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;


@Log4j2
@SpringBootTest
public class ConsumptionTestRepositoryTest {

  @Autowired
  private ConsumptionTestRepository repository;

  public void testFindTestAndAnswersById() {
    Long testId = 1L;
    List<Object> results = repository.findTestAndAnswersById(testId);
    results.forEach((result) -> {
      Object[] arr = (Object[]) result;
      ConsumptionTest test = (ConsumptionTest) arr[0];
      ConsumptionTestAnswer answer = (ConsumptionTestAnswer) arr[1];
      assertNotNull(test);
      assertNotNull(answer);
      assertEquals(testId, test.getId());
      log.info("Test: {}, Ans: {}", test, answer);
    });
  }
}
