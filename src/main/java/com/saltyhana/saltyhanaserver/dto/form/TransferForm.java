package com.saltyhana.saltyhanaserver.dto.form;


import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TransferForm {

  private Long accountId;
  private LocalDateTime tranTime;
  private Integer inOutType;
  private String printedContent;
  private Integer tranAmt;
  private Integer afterBalanceAmt;
  private String branchName;
}
