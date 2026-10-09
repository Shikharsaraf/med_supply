package com.medsupply.repository;

import com.medsupply.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByFacilityIdAndMedicineId(Long facilityId, Long medicineId);

    List<Inventory> findByFacilityId(Long facilityId);

    List<Inventory> findByMedicineId(Long medicineId);

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minimumLevel")
    List<Inventory> findBelowMinimumLevel();

    @Query("SELECT i FROM Inventory i WHERE i.medicine.id = :medicineId AND i.quantity > i.minimumLevel")
    List<Inventory> findSurplusCandidatesForMedicine(@Param("medicineId") Long medicineId);
}