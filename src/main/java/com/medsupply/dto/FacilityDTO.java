package com.medsupply.dto;

import com.medsupply.model.FacilityType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FacilityDTO {
    private Long id;

    @NotBlank(message = "Facility name is required")
    private String name;

    @NotNull(message = "Facility type is required")
    private FacilityType facilityType;

    @NotBlank(message = "District is required")
    private String district;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    public FacilityDTO() {}

    public FacilityDTO(Long id, String name, FacilityType facilityType, String district, Double latitude, Double longitude, Integer capacity) {
        this.id = id;
        this.name = name;
        this.facilityType = facilityType;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public FacilityType getFacilityType() { return facilityType; }
    public void setFacilityType(FacilityType facilityType) { this.facilityType = facilityType; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}