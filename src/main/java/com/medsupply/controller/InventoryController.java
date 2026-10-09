package com.medsupply.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medsupply.dto.ConsumptionDTO;
import com.medsupply.dto.InventoryDTO;
import com.medsupply.service.ConsumptionService;
import com.medsupply.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ConsumptionService consumptionService;

    public InventoryController(InventoryService inventoryService, ConsumptionService consumptionService) {
        this.inventoryService = inventoryService;
        this.consumptionService = consumptionService;
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> upsertInventory(@Valid @RequestBody InventoryDTO dto) {
        InventoryDTO saved = inventoryService.upsertInventory(dto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<InventoryDTO>> getByFacility(@PathVariable Long facilityId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFacility(facilityId));
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<InventoryDTO>> getByMedicine(@PathVariable Long medicineId) {
        return ResponseEntity.ok(inventoryService.getInventoryByMedicine(medicineId));
    }

    @GetMapping("/facility/{facilityId}/medicine/{medicineId}")
    public ResponseEntity<InventoryDTO> getRecord(
            @PathVariable Long facilityId,
            @PathVariable Long medicineId) {
        return ResponseEntity.ok(inventoryService.getInventoryRecord(facilityId, medicineId));
    }

    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockAlerts() {
        return ResponseEntity.ok(inventoryService.getLowStockInventories());
    }

    @PostMapping("/consume")
    public ResponseEntity<ConsumptionDTO> recordConsumption(@Valid @RequestBody ConsumptionDTO dto) {
        ConsumptionDTO recorded = consumptionService.recordConsumption(dto);
        return new ResponseEntity<>(recorded, HttpStatus.CREATED);
    }
}