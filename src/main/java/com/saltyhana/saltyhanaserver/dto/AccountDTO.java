package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private Long id;
    private String accountAlias;
    private Long accountNumber;
    private String accountType;
    private Integer accountBalance;
}
