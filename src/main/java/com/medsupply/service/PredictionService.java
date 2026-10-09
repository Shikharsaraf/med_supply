package com.medsupply.service;

import com.medsupply.dto.PredictionDTO;
import com.medsupply.exception.ResourceNotFoundException;
import com.medsupply.model.*;
import com.medsupply.repository.FacilityRepository;
import com.medsupply.repository.InventoryRepository;
import com.medsupply.repository.MedicineRepository;
import com.medsupply.repository.PredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PredictionService {

    private static final int DEFAULT_ANALYSIS_WINDOW_DAYS = 30;

    private final PredictionRepository predictionRepository;
    private final InventoryRepository inventoryRepository;
    private final FacilityRepository facilityRepository;
    private final MedicineRepository medicineRepository;
    private final ConsumptionService consumptionService;

    public PredictionService(PredictionRepository predictionRepository,
                             InventoryRepository inventoryRepository,
                             FacilityRepository facilityRepository,
                             MedicineRepository medicineRepository,
                             ConsumptionService consumptionService) {
        this.predictionRepository = predictionRepository;
        this.inventoryRepository = inventoryRepository;
        this.facilityRepository = facilityRepository;
        this.medicineRepository = medicineRepository;
        this.consumptionService = consumptionService;
    }
    public PredictionDTO mapToDTO(Prediction p) {
        return new PredictionDTO(
                p.getId(),
                p.getFacility().getId(),
                p.getFacility().getName(),
                p.getMedicine().getId(),
                p.getMedicine().getName(),
                p.getForecastDate(),
                p.getAverageDailyConsumption(),
                p.getEstimatedDaysRemaining(),
                p.getRiskLevel()
        );
    }

    public Prediction generatePrediction(Long facilityId, Long medicineId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + facilityId));

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + medicineId));

        Inventory inventory = inventoryRepository.findByFacilityIdAndMedicineId(facilityId, medicineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for facility " + facilityId + " and medicine " + medicineId));

        double adc = consumptionService.calculateAverageDailyConsumption(facilityId, medicineId, DEFAULT_ANALYSIS_WINDOW_DAYS);
        int currentStock = inventory.getQuantity();

        double daysRemaining;
        RiskLevel risk;

        if (adc <= 0.0) {
            // No consumption history; stock is effectively not depleting
            daysRemaining = currentStock > 0 ? 999.0 : 0.0;
            risk = currentStock > 0 ? RiskLevel.LOW : RiskLevel.CRITICAL;
        } else {
            daysRemaining = currentStock / adc;
            if (daysRemaining <= 3.0) {
                risk = RiskLevel.CRITICAL;
            } else if (daysRemaining <= 7.0) {
                risk = RiskLevel.HIGH;
            } else if (daysRemaining <= 14.0) {
                risk = RiskLevel.MEDIUM;
            } else {
                risk = RiskLevel.LOW;
            }
        }

        Prediction prediction = new Prediction(
                null,
                facility,
                medicine,
                LocalDate.now(),
                adc,
                Math.round(daysRemaining * 10.0) / 10.0,
                risk
        );

        return predictionRepository.save(prediction);
    }

    public List<Prediction> runSystemWideForecasts() {
        List<Inventory> allInventories = inventoryRepository.findAll();
        List<Prediction> generated = new ArrayList<>();

        for (Inventory inv : allInventories) {
            Prediction pred = generatePrediction(inv.getFacility().getId(), inv.getMedicine().getId());
            generated.add(pred);
        }

        return generated;
    }

    @Transactional(readOnly = true)
    public List<Prediction> getHighRiskAlerts() {
        return predictionRepository.findByRiskLevelIn(List.of(RiskLevel.CRITICAL, RiskLevel.HIGH));
    }

    @Transactional(readOnly = true)
    public List<Inventory> findSurplusFacilities(Long medicineId, double minDaysThreshold) {
        List<Inventory> inventories = inventoryRepository.findByMedicineId(medicineId);
        List<Inventory> surplusList = new ArrayList<>();

        for (Inventory inv : inventories) {
            double adc = consumptionService.calculateAverageDailyConsumption(
                    inv.getFacility().getId(), medicineId, DEFAULT_ANALYSIS_WINDOW_DAYS);

            if (adc > 0.0) {
                double daysRemaining = inv.getQuantity() / adc;
                if (daysRemaining >= minDaysThreshold) {
                    surplusList.add(inv);
                }
            } else if (inv.getQuantity() > inv.getMinimumLevel() * 2) {
                surplusList.add(inv);
            }
        }

        return surplusList;
    }
}