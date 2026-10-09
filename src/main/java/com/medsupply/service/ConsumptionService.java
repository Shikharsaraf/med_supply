package com.medsupply.service;

import com.medsupply.dto.ConsumptionDTO;
import com.medsupply.dto.ConsumptionRecordRequest;
import com.medsupply.model.ConsumptionRecord;
import com.medsupply.model.Facility;
import com.medsupply.model.Medicine;
import com.medsupply.repository.ConsumptionRecordRepository;
import com.medsupply.repository.FacilityRepository;
import com.medsupply.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumptionService {

    private final ConsumptionRecordRepository consumptionRepository;
    private final FacilityRepository facilityRepository;
    private final MedicineRepository medicineRepository;

    public ConsumptionService(
            ConsumptionRecordRepository consumptionRepository,
            FacilityRepository facilityRepository,
            MedicineRepository medicineRepository
    ) {
        this.consumptionRepository = consumptionRepository;
        this.facilityRepository = facilityRepository;
        this.medicineRepository = medicineRepository;
    }

    @Transactional
    public ConsumptionRecord logConsumption(Long facilityId, Long medicineId, Integer quantity, LocalDate date) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found"));

        ConsumptionRecord record = new ConsumptionRecord();
        record.setFacility(facility);
        record.setMedicine(medicine);
        record.setQuantityConsumed(quantity);
        record.setConsumptionDate(date != null ? date : LocalDate.now());

        return consumptionRepository.save(record);
    }

    @Transactional
    public ConsumptionRecord recordConsumption(ConsumptionDTO dto) {
        return logConsumption(
                dto.getFacilityId(),
                dto.getMedicineId(),
                dto.getQuantityConsumed(),
                dto.getConsumptionDate()
        );
    }

    public double calculateAverageDailyConsumption(Long facilityId, Long medicineId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<ConsumptionRecord> records = consumptionRepository
                .findByFacilityIdAndMedicineIdAndConsumptionDateAfter(facilityId, medicineId, startDate);

        if (records == null || records.isEmpty()) {
            return 0.0;
        }

        int totalUnits = records.stream()
                .mapToInt(ConsumptionRecord::getQuantityConsumed)
                .sum();

        return (double) totalUnits / days;
    }
}
