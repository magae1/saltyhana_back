package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.dto.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a.accountAlias, a.accountNum, a.accountType, a.accountBalance FROM Account a WHERE a.user.id = :userId")
    List<AccountDTO> findByUserId(@Param("userId") Long userId);
}
