package com.medsupply.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @Column(name = "average_daily_consumption", nullable = false)
    private Double averageDailyConsumption;

    @Column(name = "estimated_days_remaining", nullable = false)
    private Double estimatedDaysRemaining;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Prediction() {}

    public Prediction(Long id, Facility facility, Medicine medicine, LocalDate forecastDate,
                      Double averageDailyConsumption, Double estimatedDaysRemaining, RiskLevel riskLevel) {
        this.id = id;
        this.facility = facility;
        this.medicine = medicine;
        this.forecastDate = forecastDate;
        this.averageDailyConsumption = averageDailyConsumption;
        this.estimatedDaysRemaining = estimatedDaysRemaining;
        this.riskLevel = riskLevel;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }

    public Double getAverageDailyConsumption() { return averageDailyConsumption; }
    public void setAverageDailyConsumption(Double averageDailyConsumption) { this.averageDailyConsumption = averageDailyConsumption; }

    public Double getEstimatedDaysRemaining() { return estimatedDaysRemaining; }
    public void setEstimatedDaysRemaining(Double estimatedDaysRemaining) { this.estimatedDaysRemaining = estimatedDaysRemaining; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}