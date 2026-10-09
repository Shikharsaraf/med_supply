package com.medsupply.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "consumption_records", indexes = {
    @Index(name = "idx_consumption_fac_med_date", columnList = "facility_id, medicine_id, consumption_date")
})
public class ConsumptionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "consumption_date", nullable = false)
    private LocalDate consumptionDate;

    @Column(name = "quantity_consumed", nullable = false)
    private Integer quantityConsumed;

    public ConsumptionRecord() {}

    public ConsumptionRecord(Long id, Facility facility, Medicine medicine, LocalDate consumptionDate, Integer quantityConsumed) {
        this.id = id;
        this.facility = facility;
        this.medicine = medicine;
        this.consumptionDate = consumptionDate;
        this.quantityConsumed = quantityConsumed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Facility getFacility() { return facility; }
    public void setFacility(Facility facility) { this.facility = facility; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public LocalDate getConsumptionDate() { return consumptionDate; }
    public void setConsumptionDate(LocalDate consumptionDate) { this.consumptionDate = consumptionDate; }

    public Integer getQuantityConsumed() { return quantityConsumed; }
    public void setQuantityConsumed(Integer quantityConsumed) { this.quantityConsumed = quantityConsumed; }
}