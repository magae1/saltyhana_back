package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionTestRepository extends JpaRepository<ConsumptionTest, Long> {
    //Get으로 n번째 소비성향테스트 질문 페이지를 가져오는 쿼리
    @Query("select t from ConsumptionTest t where t.id=:Id")
    ConsumptionTest findTestById(Long Id);

    //페이지에서 응답한 내용 가져오는 쿼리
    @Query("select t from ConsumptionTest t left join ConsumptionTestAnswer ta on t.id=ta.testId.id")
    ConsumptionTestAnswer findResultById(ConsumptionTest testId);

    @Query("SELECT t, a FROM ConsumptionTest t LEFT JOIN ConsumptionTestAnswer a ON t.id=a.testId.id")
    List<Object> findAllTests();

    @Query("SELECT t, a "
        + "FROM ConsumptionTest t "
        + "LEFT JOIN ConsumptionTestAnswer a "
        + "ON t.id=a.testId.id WHERE t.id=:testId")
    List<Object> findTestAndAnswersById(Long testId);
}
