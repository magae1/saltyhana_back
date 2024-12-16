package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

/**
    [
        {
         "date": "2024-11-15",
         "balance": 1000000,
         "totalDeposit": 500000,
         "totalWithdrawal": 200000
        },
        {
         "date": "2024-11-16",
         "balance": 1050000,
         "totalDeposit": 700000,
         "totalWithdrawal": 100000
        },
        // 더 많은 날짜들...
    ]
 */

@Data
@Builder
public class TransferDTO {
    private String date;
    private Integer balance;
    private Integer totalDeposit;
    private Integer totalWithdrawal;

    // 생성자, getter, setter 생략

    public TransferDTO(String date, Integer balance, Integer totalDeposit, Integer totalWithdrawal) {
        this.date = date;
        this.balance = balance;
        this.totalDeposit = totalDeposit;
        this.totalWithdrawal = totalWithdrawal;
    }
}
