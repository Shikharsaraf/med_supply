package com.medsupply.dto;

import java.time.LocalDate;

import com.medsupply.model.RiskLevel;

public class PredictionDTO {
    private Long id;
    private Long facilityId;
    private String facilityName;
    private Long medicineId;
    private String medicineName;
    private LocalDate forecastDate;
    private Double averageDailyConsumption;
    private Double estimatedDaysRemaining;
    private RiskLevel riskLevel;

    public PredictionDTO() {}

    public PredictionDTO(Long id, Long facilityId, String facilityName, Long medicineId, String medicineName,
                         LocalDate forecastDate, Double averageDailyConsumption, Double estimatedDaysRemaining,
                         RiskLevel riskLevel) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.forecastDate = forecastDate;
        this.averageDailyConsumption = averageDailyConsumption;
        this.estimatedDaysRemaining = estimatedDaysRemaining;
        this.riskLevel = riskLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacilityId() { return facilityId; }
    public void setFacilityId(Long facilityId) { this.facilityId = facilityId; }

    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }

    public Double getAverageDailyConsumption() { return averageDailyConsumption; }
    public void setAverageDailyConsumption(Double averageDailyConsumption) { this.averageDailyConsumption = averageDailyConsumption; }

    public Double getEstimatedDaysRemaining() { return estimatedDaysRemaining; }
    public void setEstimatedDaysRemaining(Double estimatedDaysRemaining) { this.estimatedDaysRemaining = estimatedDaysRemaining; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
}