package com.medsupply.repository;

import com.medsupply.model.Shipment;
import com.medsupply.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByStatus(ShipmentStatus status);
    List<Shipment> findBySourceFacilityId(Long sourceFacilityId);
    List<Shipment> findByDestinationFacilityId(Long destinationFacilityId);
    List<Shipment> findByDestinationFacilityIdAndMedicineIdAndStatusIn(
            Long destinationFacilityId, Long medicineId, List<ShipmentStatus> statuses);
}