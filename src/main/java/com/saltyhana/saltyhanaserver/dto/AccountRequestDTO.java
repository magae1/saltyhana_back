package com.saltyhana.saltyhanaserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequestDTO {
    private Long userId;
    private String startDate;
    private String endDate;
}
