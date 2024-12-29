package com.saltyhana.saltyhanaserver.repository;

import com.saltyhana.saltyhanaserver.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@SpringBootTest
@Log4j2
class TransferRepositoryTest {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // 트랜잭션 롤백 방지
    void testSaveTransfer() {

        Account account = accountRepository.findById(5L).orElse(null);
        log.info("--로그 확인1--"+account.getId());
        Transfer transfer = Transfer.builder()
                .account(account)
                .tranTime(LocalDateTime.now())
                .inOutType(0)
                .printedContent("트래블로그 적금")
                .tranAmt(10)
                .afterBalanceAmt(0)
                .branchName("기도브랜치")
                .build();
        log.info("--로그 확인2--"+transfer.getPrintedContent());

        try{
            transferRepository.save(transfer);
            transferRepository.flush();
        }catch(Exception e){
            entityManager.clear();
        }
    }


}
