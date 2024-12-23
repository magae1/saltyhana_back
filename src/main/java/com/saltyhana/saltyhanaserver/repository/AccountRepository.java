package com.saltyhana.saltyhanaserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 주거래 통장을 제일 위에 오도록 정렬
    @Query("SELECT new com.saltyhana.saltyhanaserver.dto.AccountDTO(a.id, a.accountAlias, a.accountNum, a.accountType, a.main) FROM Account a WHERE a.user.id = :userId ORDER BY a.main DESC")
    List<AccountDTO> findAllByUserId(@Param("userId") Long userId);
}
