package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountResponseDTO {
    private String accountNumber;
    private String accountAlias;
    private Integer accountBalance;
    private List<TransferDTO> transferList;
}
