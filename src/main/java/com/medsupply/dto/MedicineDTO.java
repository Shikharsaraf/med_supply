package com.medsupply.dto;

import com.medsupply.model.Criticality;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicineDTO {
    private Long id;

    @NotBlank(message = "Medicine name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Criticality is required")
    private Criticality criticality;

    private Integer shelfLifeDays;
    private String storageRequirements;

    public MedicineDTO() {}

    public MedicineDTO(Long id, String name, String category, String unit, Criticality criticality, Integer shelfLifeDays, String storageRequirements) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.criticality = criticality;
        this.shelfLifeDays = shelfLifeDays;
        this.storageRequirements = storageRequirements;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Criticality getCriticality() { return criticality; }
    public void setCriticality(Criticality criticality) { this.criticality = criticality; }

    public Integer getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(Integer shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }

    public String getStorageRequirements() { return storageRequirements; }
    public void setStorageRequirements(String storageRequirements) { this.storageRequirements = storageRequirements; }
}