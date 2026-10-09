package com.medsupply.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medsupply.dto.ShipmentDTO;
import com.medsupply.model.ShipmentStatus;
import com.medsupply.service.ShipmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ShipmentDTO> requestShipment(@Valid @RequestBody ShipmentDTO dto) {
        ShipmentDTO created = shipmentService.createShipment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getShipments(
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) Long destinationFacilityId) {

        if (status != null) {
            return ResponseEntity.ok(shipmentService.getShipmentsByStatus(status));
        }
        if (destinationFacilityId != null) {
            return ResponseEntity.ok(shipmentService.getShipmentsByDestination(destinationFacilityId));
        }
        return ResponseEntity.ok(shipmentService.getShipmentsByStatus(ShipmentStatus.REQUESTED));
    }

    @PostMapping("/{id}/dispatch")
    public ResponseEntity<ShipmentDTO> dispatchShipment(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.dispatchShipment(id));
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<ShipmentDTO> deliverShipment(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.deliverShipment(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ShipmentDTO> cancelShipment(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.cancelShipment(id));
    }
}