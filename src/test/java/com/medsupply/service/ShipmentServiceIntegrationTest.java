package com.medsupply.service;

import com.medsupply.dto.ShipmentDTO;
import com.medsupply.exception.InsufficientStockException;
import com.medsupply.model.*;
import com.medsupply.repository.FacilityRepository;
import com.medsupply.repository.InventoryRepository;
import com.medsupply.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShipmentServiceIntegrationTest {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    private Facility warehouse;
    private Facility clinic;
    private Medicine amoxicillin;

    @BeforeEach
    void setUp() {
        warehouse = new Facility();
        warehouse.setName("Hub A");
        warehouse.setFacilityType(FacilityType.REGIONAL_WAREHOUSE);
        warehouse.setDistrict("North");
        warehouse.setLatitude(28.61);
        warehouse.setLongitude(77.20);
        warehouse.setCapacity(10000);
        warehouse = facilityRepository.save(warehouse);

        clinic = new Facility();
        clinic.setName("Clinic B");
        clinic.setFacilityType(FacilityType.PRIMARY_CLINIC);
        clinic.setDistrict("South");
        clinic.setLatitude(28.50);
        clinic.setLongitude(77.10);
        clinic.setCapacity(2000);
        clinic = facilityRepository.save(clinic);

        amoxicillin = new Medicine();
        amoxicillin.setName("Amoxicillin Test");
        amoxicillin.setCategory("Antibiotic");
        amoxicillin.setUnit("Capsules");
        amoxicillin.setCriticality(Criticality.HIGH);
        amoxicillin.setShelfLifeDays(365);
        amoxicillin.setStorageRequirements("Standard");
        amoxicillin = medicineRepository.save(amoxicillin);

        // Warehouse inventory: 500 units
        Inventory warehouseInv = new Inventory();
        warehouseInv.setFacility(warehouse);
        warehouseInv.setMedicine(amoxicillin);
        warehouseInv.setQuantity(500);
        warehouseInv.setMinimumLevel(50);
        warehouseInv.setMaximumLevel(1000);
        warehouseInv.setExpiryDate(LocalDate.now().plusMonths(6));
        inventoryRepository.save(warehouseInv);

        // Clinic inventory: 20 units
        Inventory clinicInv = new Inventory();
        clinicInv.setFacility(clinic);
        clinicInv.setMedicine(amoxicillin);
        clinicInv.setQuantity(20);
        clinicInv.setMinimumLevel(50);
        clinicInv.setMaximumLevel(200);
        clinicInv.setExpiryDate(LocalDate.now().plusMonths(6));
        inventoryRepository.save(clinicInv);
    }

    @Test
    @DisplayName("Should successfully transfer inventory through dispatch and delivery lifecycle")
    void testShipmentLifecycleInventoryUpdates() {
        ShipmentDTO request = new ShipmentDTO();
        request.setSourceFacilityId(warehouse.getId());
        request.setDestinationFacilityId(clinic.getId());
        request.setMedicineId(amoxicillin.getId());
        request.setQuantity(100);
        request.setReason("Rebalancing stock");

        // 1. Create Shipment
        ShipmentDTO created = shipmentService.createShipment(request);
        assertNotNull(created.getId());
        assertEquals(ShipmentStatus.REQUESTED, created.getStatus());

        // 2. Dispatch Shipment (Warehouse: 500 -> 400)
        ShipmentDTO dispatched = shipmentService.dispatchShipment(created.getId());
        assertEquals(ShipmentStatus.IN_TRANSIT, dispatched.getStatus());

        Inventory warehouseInv = inventoryRepository
                .findByFacilityIdAndMedicineId(warehouse.getId(), amoxicillin.getId()).orElseThrow();
        assertEquals(400, warehouseInv.getQuantity());

        // 3. Deliver Shipment (Clinic: 20 -> 120)
        ShipmentDTO delivered = shipmentService.deliverShipment(created.getId());
        assertEquals(ShipmentStatus.DELIVERED, delivered.getStatus());

        Inventory clinicInv = inventoryRepository
                .findByFacilityIdAndMedicineId(clinic.getId(), amoxicillin.getId()).orElseThrow();
        assertEquals(120, clinicInv.getQuantity());
    }

    @Test
    @DisplayName("Should reject shipment dispatch when source facility lacks sufficient inventory")
    void testDispatchThrowsExceptionOnInsufficientStock() {
        ShipmentDTO request = new ShipmentDTO();
        request.setSourceFacilityId(warehouse.getId());
        request.setDestinationFacilityId(clinic.getId());
        request.setMedicineId(amoxicillin.getId());
        request.setQuantity(600); // Exceeds warehouse's 500 units
        request.setReason("Over-requested stock");

        ShipmentDTO created = shipmentService.createShipment(request);

        assertThrows(InsufficientStockException.class, () -> {
            shipmentService.dispatchShipment(created.getId());
        });

        // Verify warehouse stock remains untouched
        Inventory warehouseInv = inventoryRepository
                .findByFacilityIdAndMedicineId(warehouse.getId(), amoxicillin.getId()).orElseThrow();
        assertEquals(500, warehouseInv.getQuantity());
    }
}