package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // 일일 입출금 내역을 가져오는 쿼리
    @Query("SELECT FUNCTION('DATE', t.tranTime) as date, " +  // 여기서 'DATE'는 데이터베이스에서 지원되는 날짜 추출 함수입니다.
            "SUM(CASE WHEN t.inOutType = 0 THEN t.tranAmt ELSE 0 END) AS totalDeposit, " +
            "SUM(CASE WHEN t.inOutType = 1 THEN t.tranAmt ELSE 0 END) AS totalWithdrawal " +
            "FROM Transfer t " +
            "WHERE t.account.id = :accountId " +
            "AND t.tranTime BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', t.tranTime)")  // 날짜만 그룹화
    List<Object[]> findDailyTransactions(@Param("accountId") Long accountId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    // 일일 잔액을 가져오는 쿼리
    @Query("SELECT FUNCTION('DATE', t.tranTime) as date, t.afterBalanceAmt " +
            "FROM Transfer t " +
            "WHERE t.tranTime = (" +
            "    SELECT MAX(t2.tranTime) " +
            "    FROM Transfer t2 " +
            "    WHERE FUNCTION('DATE', t2.tranTime) = FUNCTION('DATE', t.tranTime)) " +  // 날짜만 비교
            "AND t.account.id = :accountId " +
            "AND t.tranTime BETWEEN :startDate AND :endDate")
    List<Object[]> findDailyBalance(@Param("accountId") Long accountId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}
