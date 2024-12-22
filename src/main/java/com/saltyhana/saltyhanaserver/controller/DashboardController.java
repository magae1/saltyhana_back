package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.DashBoardResponseDTO;
import com.saltyhana.saltyhanaserver.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/home")
    public List<DashBoardResponseDTO> getDashboard(){
        return dashboardService.getGoalsAndWeekdays();
    }
}
