package com.medsupply.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medsupply.dto.InventoryDTO;
import com.medsupply.exception.InsufficientStockException;
import com.medsupply.exception.ResourceNotFoundException;
import com.medsupply.model.Facility;
import com.medsupply.model.Inventory;
import com.medsupply.model.Medicine;
import com.medsupply.repository.FacilityRepository;
import com.medsupply.repository.InventoryRepository;
import com.medsupply.repository.MedicineRepository;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final FacilityRepository facilityRepository;
    private final MedicineRepository medicineRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            FacilityRepository facilityRepository,
                            MedicineRepository medicineRepository) {
        this.inventoryRepository = inventoryRepository;
        this.facilityRepository = facilityRepository;
        this.medicineRepository = medicineRepository;
    }

    public InventoryDTO upsertInventory(InventoryDTO dto) {
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + dto.getFacilityId()));

        Medicine medicine = medicineRepository.findById(dto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + dto.getMedicineId()));

        Inventory inventory = inventoryRepository.findByFacilityIdAndMedicineId(dto.getFacilityId(), dto.getMedicineId())
                .map(existing -> {
                    existing.setQuantity(dto.getQuantity());
                    existing.setMinimumLevel(dto.getMinimumLevel());
                    existing.setMaximumLevel(dto.getMaximumLevel());
                    existing.setExpiryDate(dto.getExpiryDate());
                    return existing;
                })
                .orElseGet(() -> new Inventory(
                        null,
                        facility,
                        medicine,
                        dto.getQuantity(),
                        dto.getMinimumLevel(),
                        dto.getMaximumLevel(),
                        dto.getExpiryDate()
                ));

        Inventory saved = inventoryRepository.save(inventory);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<InventoryDTO> getInventoryByFacility(Long facilityId) {
        if (!facilityRepository.existsById(facilityId)) {
            throw new ResourceNotFoundException("Facility not found with id: " + facilityId);
        }
        return inventoryRepository.findByFacilityId(facilityId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryDTO> getInventoryByMedicine(Long medicineId) {
        if (!medicineRepository.existsById(medicineId)) {
            throw new ResourceNotFoundException("Medicine not found with id: " + medicineId);
        }
        return inventoryRepository.findByMedicineId(medicineId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventoryDTO getInventoryRecord(Long facilityId, Long medicineId) {
        Inventory inventory = inventoryRepository.findByFacilityIdAndMedicineId(facilityId, medicineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for facility " + facilityId + " and medicine " + medicineId));
        return mapToDTO(inventory);
    }

    @Transactional(readOnly = true)
    public List<InventoryDTO> getLowStockInventories() {
        return inventoryRepository.findBelowMinimumLevel().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deductStock(Long facilityId, Long medicineId, int quantity) {
        Inventory inventory = inventoryRepository.findByFacilityIdAndMedicineId(facilityId, medicineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for facility " + facilityId + " and medicine " + medicineId));

        if (inventory.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock at facility " + facilityId + ". Current: " + inventory.getQuantity() + ", requested: " + quantity);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    public void addStock(Long facilityId, Long medicineId, int quantity) {
        Inventory inventory = inventoryRepository.findByFacilityIdAndMedicineId(facilityId, medicineId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for facility " + facilityId + " and medicine " + medicineId));

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    public InventoryDTO mapToDTO(Inventory inventory) {
        return new InventoryDTO(
                inventory.getId(),
                inventory.getFacility().getId(),
                inventory.getFacility().getName(),
                inventory.getMedicine().getId(),
                inventory.getMedicine().getName(),
                inventory.getQuantity(),
                inventory.getMinimumLevel(),
                inventory.getMaximumLevel(),
                inventory.getExpiryDate()
        );
    }
}