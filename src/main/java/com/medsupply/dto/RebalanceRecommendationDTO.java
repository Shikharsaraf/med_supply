package com.medsupply.dto;

public class RebalanceRecommendationDTO {
    private Long medicineId;
    private String medicineName;
    private Long sourceFacilityId;
    private String sourceFacilityName;
    private Long targetFacilityId;
    private String targetFacilityName;
    private Integer transferQuantity;
    private Double distanceKm;
    private Long shipmentId;

    public RebalanceRecommendationDTO() {}

    public RebalanceRecommendationDTO(Long medicineId, String medicineName, Long sourceFacilityId,
                                      String sourceFacilityName, Long targetFacilityId, String targetFacilityName,
                                      Integer transferQuantity, Double distanceKm, Long shipmentId) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.sourceFacilityId = sourceFacilityId;
        this.sourceFacilityName = sourceFacilityName;
        this.targetFacilityId = targetFacilityId;
        this.targetFacilityName = targetFacilityName;
        this.transferQuantity = transferQuantity;
        this.distanceKm = distanceKm;
        this.shipmentId = shipmentId;
    }

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public Long getSourceFacilityId() { return sourceFacilityId; }
    public void setSourceFacilityId(Long sourceFacilityId) { this.sourceFacilityId = sourceFacilityId; }

    public String getSourceFacilityName() { return sourceFacilityName; }
    public void setSourceFacilityName(String sourceFacilityName) { this.sourceFacilityName = sourceFacilityName; }

    public Long getTargetFacilityId() { return targetFacilityId; }
    public void setTargetFacilityId(Long targetFacilityId) { this.targetFacilityId = targetFacilityId; }

    public String getTargetFacilityName() { return targetFacilityName; }
    public void setTargetFacilityName(String targetFacilityName) { this.targetFacilityName = targetFacilityName; }

    public Integer getTransferQuantity() { return transferQuantity; }
    public void setTransferQuantity(Integer transferQuantity) { this.transferQuantity = transferQuantity; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
}