package com.medsupply.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medsupply.dto.InventoryDTO;
import com.medsupply.dto.PredictionDTO;
import com.medsupply.model.Inventory;
import com.medsupply.model.Prediction;
import com.medsupply.service.InventoryService;
import com.medsupply.service.PredictionService;

@RestController
@RequestMapping("/api/v1/predictions")
public class PredictionController {

    private final PredictionService predictionService;
    private final InventoryService inventoryService;

    public PredictionController(PredictionService predictionService, InventoryService inventoryService) {
        this.predictionService = predictionService;
        this.inventoryService = inventoryService;
    }

    @PostMapping("/calculate/facility/{facilityId}/medicine/{medicineId}")
    public ResponseEntity<PredictionDTO> calculatePrediction(
            @PathVariable Long facilityId,
            @PathVariable Long medicineId) {
        Prediction prediction = predictionService.generatePrediction(facilityId, medicineId);
        return ResponseEntity.ok(predictionService.mapToDTO(prediction));
    }

    @PostMapping("/run-batch")
    public ResponseEntity<List<PredictionDTO>> runSystemWideForecasts() {
        List<Prediction> predictions = predictionService.runSystemWideForecasts();
        List<PredictionDTO> dtos = predictions.stream()
                .map(predictionService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/alerts/stock-out")
    public ResponseEntity<List<PredictionDTO>> getStockOutAlerts() {
        List<Prediction> alerts = predictionService.getHighRiskAlerts();
        List<PredictionDTO> dtos = alerts.stream()
                .map(predictionService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/surplus/medicine/{medicineId}")
    public ResponseEntity<List<InventoryDTO>> getSurplusFacilities(
            @PathVariable Long medicineId,
            @RequestParam(defaultValue = "30.0") double minDaysThreshold) {
        List<Inventory> surplus = predictionService.findSurplusFacilities(medicineId, minDaysThreshold);
        List<InventoryDTO> dtos = surplus.stream()
                .map(inventoryService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}