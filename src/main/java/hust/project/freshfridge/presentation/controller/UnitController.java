package hust.project.freshfridge.presentation.controller;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.application.usecase.UnitUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unit")
@RequiredArgsConstructor
public class UnitController {

    private final UnitUseCase unitUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<UnitListResponse>> getAllUnits() {
        UnitListResponse response = unitUseCase.getAllUnits();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
