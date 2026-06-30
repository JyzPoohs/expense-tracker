package com.expense.tracker.controller;

import com.expense.tracker.dto.DashboardSummaryDTO;
import com.expense.tracker.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestParam(required = false) String type,
                                                                   @RequestParam(required = false) String category,
                                                                   @RequestParam(required = false) Integer month,
                                                                   @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(summaryService.getDashboardSummary(jwt, type, category, month, year));
    }
}
