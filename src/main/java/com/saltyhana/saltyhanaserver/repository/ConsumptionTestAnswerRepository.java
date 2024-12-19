package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.ConsumptionTest;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsumptionTestAnswerRepository extends JpaRepository<ConsumptionTestAnswer, ConsumptionTest> { //Long vs ConsumptionTest?
    //모든 ConsumptionTest와 연결된 ConsumptionTestAnswer를 포함시키려고?
    @Query("select t from ConsumptionTest t left join ConsumptionTestAnswer ta on t.id=ta.testId.id")
    List<ConsumptionTestAnswer>getAll();

    @Query("select testId.id from ConsumptionTestAnswer")
    ConsumptionTestAnswer findByQuestionNum(Integer questionNum);

    //

}
