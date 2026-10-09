package com.medsupply.controller;

import com.medsupply.dto.RebalanceRecommendationDTO;
import com.medsupply.service.RebalancingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rebalance")
public class RebalancingController {

    private final RebalancingService rebalancingService;

    public RebalancingController(RebalancingService rebalancingService) {
        this.rebalancingService = rebalancingService;
    }

    @PostMapping("/execute")
    public ResponseEntity<List<RebalanceRecommendationDTO>> triggerRebalancing(
            @RequestParam(defaultValue = "true") boolean autoCreateShipments) {
        List<RebalanceRecommendationDTO> results = rebalancingService.executeAutoRebalance(autoCreateShipments);
        return ResponseEntity.ok(results);
    }
}