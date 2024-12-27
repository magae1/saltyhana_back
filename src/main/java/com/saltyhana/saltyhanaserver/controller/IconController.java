package com.saltyhana.saltyhanaserver.controller;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.dto.IconResponseDTO;
import com.saltyhana.saltyhanaserver.service.IconService;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api_prefix}/icons")
public class IconController {

  private final IconService iconService;

  @Operation(summary = "목표 아이콘 목록 조회")
  @GetMapping("goal")
  public List<IconResponseDTO> getGoalIcons() {
    return iconService.findAllGoalIcons();
  }

}
