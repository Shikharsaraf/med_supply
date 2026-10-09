package com.medsupply.repository;

import com.medsupply.model.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, Long> {
    List<ConsumptionRecord> findByFacilityIdAndMedicineIdAndConsumptionDateAfter(
            Long facilityId, Long medicineId, LocalDate startDate
    );
}
