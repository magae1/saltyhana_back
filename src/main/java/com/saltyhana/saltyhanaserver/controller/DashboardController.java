package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.DashBoardRequestDTO;
import com.saltyhana.saltyhanaserver.dto.DashBoardResponseDTO;
import com.saltyhana.saltyhanaserver.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/home")
    public List<DashBoardResponseDTO> getDashboard(){
        return dashboardService.getGoalsAndWeekdays();
    }
}
