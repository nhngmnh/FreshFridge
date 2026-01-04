package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.response.ApiResponse;
import hust.project.freshfridge.application.dto.response.report.*;
import hust.project.freshfridge.application.usecase.ReportUseCase;
import hust.project.freshfridge.infrastructure.security.CurrentUser;
import hust.project.freshfridge.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportUseCase reportUseCase;

    /**
     * Get overview report
     * GET /reports/overview?period=month&topItemsLimit=5
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<OverviewReportResponse>> getOverviewReport(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) Integer topItemsLimit) {
        OverviewReportResponse response = reportUseCase.getOverviewReport(
                principal.getId(), period, topItemsLimit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get kitchen report
     * GET /reports/kitchen?startDate=2025-12-27&endDate=2026-01-03
     */
    @GetMapping("/kitchen")
    public ResponseEntity<ApiResponse<KitchenReportResponse>> getKitchenReport(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        KitchenReportResponse response = reportUseCase.getKitchenReport(
                principal.getId(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get shopping report
     * GET /reports/shopping?period=month&frequentItemsLimit=5
     */
    @GetMapping("/shopping")
    public ResponseEntity<ApiResponse<ShoppingReportResponse>> getShoppingReport(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) Integer frequentItemsLimit) {
        ShoppingReportResponse response = reportUseCase.getShoppingReport(
                principal.getId(), period, frequentItemsLimit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get meal report
     * GET /reports/meal?period=month&topRecipesLimit=5
     */
    @GetMapping("/meal")
    public ResponseEntity<ApiResponse<MealReportResponse>> getMealReport(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) Integer topRecipesLimit) {
        MealReportResponse response = reportUseCase.getMealReport(
                principal.getId(), period, topRecipesLimit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get full report (all reports combined)
     * GET /reports/full?period=month
     */
    @GetMapping("/full")
    public ResponseEntity<ApiResponse<FullReportResponse>> getFullReport(
            @CurrentUser UserPrincipal principal,
            @RequestParam(required = false, defaultValue = "month") String period) {
        FullReportResponse response = reportUseCase.getFullReport(principal.getId(), period);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
