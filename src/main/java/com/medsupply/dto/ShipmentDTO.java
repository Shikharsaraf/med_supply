package com.medsupply.dto;

import java.time.LocalDateTime;

import com.medsupply.model.ShipmentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ShipmentDTO {
    private Long id;

    @NotNull(message = "Source facility ID is required")
    private Long sourceFacilityId;
    private String sourceFacilityName;

    @NotNull(message = "Destination facility ID is required")
    private Long destinationFacilityId;
    private String destinationFacilityName;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;
    private String medicineName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Transfer quantity must be greater than zero")
    private Integer quantity;

    private ShipmentStatus status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;

    public ShipmentDTO() {}

    public ShipmentDTO(Long id, Long sourceFacilityId, String sourceFacilityName, Long destinationFacilityId, String destinationFacilityName, Long medicineId, String medicineName, Integer quantity, ShipmentStatus status, String reason, LocalDateTime createdAt, LocalDateTime dispatchedAt, LocalDateTime deliveredAt) {
        this.id = id;
        this.sourceFacilityId = sourceFacilityId;
        this.sourceFacilityName = sourceFacilityName;
        this.destinationFacilityId = destinationFacilityId;
        this.destinationFacilityName = destinationFacilityName;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSourceFacilityId() { return sourceFacilityId; }
    public void setSourceFacilityId(Long sourceFacilityId) { this.sourceFacilityId = sourceFacilityId; }

    public String getSourceFacilityName() { return sourceFacilityName; }
    public void setSourceFacilityName(String sourceFacilityName) { this.sourceFacilityName = sourceFacilityName; }

    public Long getDestinationFacilityId() { return destinationFacilityId; }
    public void setDestinationFacilityId(Long destinationFacilityId) { this.destinationFacilityId = destinationFacilityId; }

    public String getDestinationFacilityName() { return destinationFacilityName; }
    public void setDestinationFacilityName(String destinationFacilityName) { this.destinationFacilityName = destinationFacilityName; }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}