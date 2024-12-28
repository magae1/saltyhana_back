package com.saltyhana.saltyhanaserver.mapper;


import com.saltyhana.saltyhanaserver.dto.form.TransferForm;
import com.saltyhana.saltyhanaserver.entity.Account;
import com.saltyhana.saltyhanaserver.entity.Transfer;


public class TransferMapper {

  public static Transfer toEntity(TransferForm transferForm, Account account) {
    return Transfer.builder()
        .account(account)
        .tranTime(transferForm.getTranTime())
        .inOutType(transferForm.getInOutType())
        .printedContent(transferForm.getPrintedContent())
        .tranAmt(transferForm.getTranAmt())
        .afterBalanceAmt(transferForm.getAfterBalanceAmt())
        .branchName(transferForm.getBranchName())
        .build();
  }
}
