package com.expense.tracker.controller;

import com.expense.tracker.dto.DashboardBarChartDTO;
import com.expense.tracker.dto.DashboardPieChartDTO;
import com.expense.tracker.service.ChartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chart")
public class ChartController {
    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/dashboard/barchart")
    public ResponseEntity<List<DashboardBarChartDTO>> getDashboardBarChartData(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chartService.getDashboardBarChartData(jwt));
    }

    @GetMapping("/dashboard/piechart")
    public ResponseEntity<List<DashboardPieChartDTO>> getDashboardPieChartData(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chartService.getDashboardPieChartData(jwt));
    }
}
