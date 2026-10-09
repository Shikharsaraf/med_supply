package com.medsupply.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class InventoryDTO {
    private Long id;

    @NotNull(message = "Facility ID is required")
    private Long facilityId;
    private String facilityName;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;
    private String medicineName;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Minimum level is required")
    @PositiveOrZero(message = "Minimum level cannot be negative")
    private Integer minimumLevel;

    private Integer maximumLevel;
    private LocalDate expiryDate;

    public InventoryDTO() {}

    public InventoryDTO(Long id, Long facilityId, String facilityName, Long medicineId, String medicineName, Integer quantity, Integer minimumLevel, Integer maximumLevel, LocalDate expiryDate) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
        this.expiryDate = expiryDate;
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

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getMinimumLevel() { return minimumLevel; }
    public void setMinimumLevel(Integer minimumLevel) { this.minimumLevel = minimumLevel; }

    public Integer getMaximumLevel() { return maximumLevel; }
    public void setMaximumLevel(Integer maximumLevel) { this.maximumLevel = maximumLevel; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}