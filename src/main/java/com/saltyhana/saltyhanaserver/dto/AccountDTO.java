package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountDTO {
    private Long id;
    private String accountAlias;
    private Long accountNumber;
    private String accountType;
    private Integer accountBalance;

    public AccountDTO(Long id, String accountAlias, Long accountNumber, String accountType, Integer accountBalance) {
        this.id = id;
        this.accountAlias = accountAlias;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }
}
