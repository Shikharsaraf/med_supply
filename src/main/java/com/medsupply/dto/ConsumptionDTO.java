package com.medsupply.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ConsumptionDTO {
    private Long id;

    @NotNull(message = "Facility ID is required")
    private Long facilityId;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Consumption date is required")
    private LocalDate consumptionDate;

    @NotNull(message = "Quantity consumed is required")
    @Positive(message = "Quantity consumed must be greater than zero")
    private Integer quantityConsumed;

    public ConsumptionDTO() {}

    public ConsumptionDTO(Long id, Long facilityId, Long medicineId, LocalDate consumptionDate, Integer quantityConsumed) {
        this.id = id;
        this.facilityId = facilityId;
        this.medicineId = medicineId;
        this.consumptionDate = consumptionDate;
        this.quantityConsumed = quantityConsumed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFacilityId() { return facilityId; }
    public void setFacilityId(Long facilityId) { this.facilityId = facilityId; }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }

    public LocalDate getConsumptionDate() { return consumptionDate; }
    public void setConsumptionDate(LocalDate consumptionDate) { this.consumptionDate = consumptionDate; }

    public Integer getQuantityConsumed() { return quantityConsumed; }
    public void setQuantityConsumed(Integer quantityConsumed) { this.quantityConsumed = quantityConsumed; }
}