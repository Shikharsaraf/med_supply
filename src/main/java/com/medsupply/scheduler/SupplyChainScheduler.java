package com.medsupply.scheduler;

import com.medsupply.dto.RebalanceRecommendationDTO;
import com.medsupply.model.Prediction;
import com.medsupply.service.PredictionService;
import com.medsupply.service.RebalancingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplyChainScheduler {

    private static final Logger log = LoggerFactory.getLogger(SupplyChainScheduler.class);

    private final PredictionService predictionService;
    private final RebalancingService rebalancingService;

    public SupplyChainScheduler(PredictionService predictionService, RebalancingService rebalancingService) {
        this.predictionService = predictionService;
        this.rebalancingService = rebalancingService;
    }

    /**
     * Runs daily at midnight:
     * 1. Re-calculates 30-day consumption forecasts across all inventories.
     * 2. Flags critical/high stock-out risks.
     * 3. Executes auto-rebalancing transfers from nearest surplus facilities.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailySupplyChainOptimization() {
        log.info("Starting scheduled daily supply chain optimization cycle...");

        List<Prediction> predictions = predictionService.runSystemWideForecasts();
        log.info("Completed forecasts for {} inventory items.", predictions.size());

        List<Prediction> criticalAlerts = predictionService.getHighRiskAlerts();
        log.warn("Identified {} facilities facing critical/high stock-out risk.", criticalAlerts.size());

        List<RebalanceRecommendationDTO> rebalanced = rebalancingService.executeAutoRebalance(true);
        log.info("Auto-rebalancing complete. Generated {} emergency transfer shipments.", rebalanced.size());
    }
}