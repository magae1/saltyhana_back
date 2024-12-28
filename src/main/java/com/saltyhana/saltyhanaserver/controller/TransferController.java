package com.saltyhana.saltyhanaserver.controller;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.dto.form.TransferForm;
import com.saltyhana.saltyhanaserver.service.TransferService;


@RestController
@RequestMapping("${api_prefix}/transfers")
@RequiredArgsConstructor
public class TransferController {

  private final TransferService transferService;

  @Operation(summary = "계좌 거래내역 hooks")
  @PostMapping
  public void postTransfer(@RequestBody List<TransferForm> transferFormList) {
    transferService.bulkInsertTransfers(transferFormList);
  }

}
