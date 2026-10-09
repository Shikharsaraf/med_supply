package com.medsupply.repository;

import com.medsupply.model.Facility;
import com.medsupply.model.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByDistrict(String district);
    List<Facility> findByFacilityType(FacilityType facilityType);
}