package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionTestRepository extends JpaRepository<ConsumptionTest, Long> {
    //Get으로 n번째 소비성향테스트 질문 페이지를 가져오는 쿼리
    @Query("select t, ta from ConsumptionTest t RIGHT join ConsumptionTestAnswer ta on t.id = ta.testId.id WHERE t.id = :id")
    List<Object[]> findTestById(Long id);

    @Query("SELECT t, a FROM ConsumptionTest t LEFT JOIN ConsumptionTestAnswer a ON t.id=a.testId.id")
    List<Object> findAllTests();

    @Query("SELECT t, a "
        + "FROM ConsumptionTest t "
        + "LEFT JOIN ConsumptionTestAnswer a "
        + "ON t.id=a.testId.id WHERE t.id=:testId")
    List<Object> findTestAndAnswersById(Long testId);
}
