package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetResponseDTO {
    private String accountNumber;
    private String accoutName;
    private Integer accountBalance;
    private List<List<Integer>> amountList;  // [0]: 일일 입금 총액, [1]: 일일 출금 총액, [2]: 일일 잔액
}
