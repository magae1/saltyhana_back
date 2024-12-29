package com.saltyhana.saltyhanaserver.listener;

import com.saltyhana.saltyhanaserver.entity.Goal;
import com.saltyhana.saltyhanaserver.entity.Transfer;
import com.saltyhana.saltyhanaserver.service.ProgressService;
import com.saltyhana.saltyhanaserver.service.TransferService;
import jakarta.persistence.PostPersist;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TransferEntityListener {

    private static TransferService transferService;

    @Deprecated
    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    //transfer insert 시 체크 후 progress 재설정
    @PostPersist
    public void afterTransferPersist(Transfer transfer) {
        log.info("afterTransferPersist 호출: {}", transfer.getId());
        transferService.checkTransfer(transfer);
    }
}
