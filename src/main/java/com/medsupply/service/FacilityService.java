package com.medsupply.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medsupply.dto.FacilityDTO;
import com.medsupply.exception.ResourceNotFoundException;
import com.medsupply.model.Facility;
import com.medsupply.model.FacilityType;
import com.medsupply.repository.FacilityRepository;

@Service
@Transactional
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public FacilityDTO createFacility(FacilityDTO dto) {
        Facility facility = mapToEntity(dto);
        Facility saved = facilityRepository.save(facility);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<FacilityDTO> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacilityDTO getFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));
        return mapToDTO(facility);
    }

    @Transactional(readOnly = true)
    public List<FacilityDTO> getFacilitiesByDistrict(String district) {
        return facilityRepository.findByDistrict(district).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacilityDTO> getFacilitiesByType(FacilityType facilityType) {
        return facilityRepository.findByFacilityType(facilityType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FacilityDTO updateFacility(Long id, FacilityDTO dto) {
        Facility existing = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with id: " + id));

        existing.setName(dto.getName());
        existing.setFacilityType(dto.getFacilityType());
        existing.setDistrict(dto.getDistrict());
        existing.setLatitude(dto.getLatitude());
        existing.setLongitude(dto.getLongitude());
        existing.setCapacity(dto.getCapacity());

        Facility updated = facilityRepository.save(existing);
        return mapToDTO(updated);
    }

    public void deleteFacility(Long id) {
        if (!facilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Facility not found with id: " + id);
        }
        facilityRepository.deleteById(id);
    }

    public Facility mapToEntity(FacilityDTO dto) {
        return new Facility(
                dto.getId(),
                dto.getName(),
                dto.getFacilityType(),
                dto.getDistrict(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getCapacity()
        );
    }

    public FacilityDTO mapToDTO(Facility facility) {
        return new FacilityDTO(
                facility.getId(),
                facility.getName(),
                facility.getFacilityType(),
                facility.getDistrict(),
                facility.getLatitude(),
                facility.getLongitude(),
                facility.getCapacity()
        );
    }
}