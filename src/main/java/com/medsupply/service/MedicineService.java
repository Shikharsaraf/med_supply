package com.medsupply.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medsupply.dto.MedicineDTO;
import com.medsupply.exception.ResourceNotFoundException;
import com.medsupply.model.Criticality;
import com.medsupply.model.Medicine;
import com.medsupply.repository.MedicineRepository;

@Service
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public MedicineDTO createMedicine(MedicineDTO dto) {
        Medicine medicine = mapToEntity(dto);
        Medicine saved = medicineRepository.save(medicine);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicineDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return mapToDTO(medicine);
    }

    @Transactional(readOnly = true)
    public List<MedicineDTO> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicineDTO> getMedicinesByCriticality(Criticality criticality) {
        return medicineRepository.findByCriticality(criticality).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MedicineDTO updateMedicine(Long id, MedicineDTO dto) {
        Medicine existing = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        existing.setName(dto.getName());
        existing.setCategory(dto.getCategory());
        existing.setUnit(dto.getUnit());
        existing.setCriticality(dto.getCriticality());
        existing.setShelfLifeDays(dto.getShelfLifeDays());
        existing.setStorageRequirements(dto.getStorageRequirements());

        Medicine updated = medicineRepository.save(existing);
        return mapToDTO(updated);
    }

    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }

    public Medicine mapToEntity(MedicineDTO dto) {
        return new Medicine(
                dto.getId(),
                dto.getName(),
                dto.getCategory(),
                dto.getUnit(),
                dto.getCriticality(),
                dto.getShelfLifeDays(),
                dto.getStorageRequirements()
        );
    }

    public MedicineDTO mapToDTO(Medicine medicine) {
        return new MedicineDTO(
                medicine.getId(),
                medicine.getName(),
                medicine.getCategory(),
                medicine.getUnit(),
                medicine.getCriticality(),
                medicine.getShelfLifeDays(),
                medicine.getStorageRequirements()
        );
    }
}