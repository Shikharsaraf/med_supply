package com.medsupply.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medsupply.model.Prediction;
import com.medsupply.model.RiskLevel;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    List<Prediction> findByFacilityId(Long facilityId);

    List<Prediction> findByMedicineId(Long medicineId);

    List<Prediction> findByRiskLevel(RiskLevel riskLevel);

    List<Prediction> findByRiskLevelIn(Collection<RiskLevel> riskLevels);

    @Query("SELECT p FROM Prediction p WHERE p.facility.id = :facilityId AND p.medicine.id = :medicineId ORDER BY p.createdAt DESC")
    List<Prediction> findLatestByFacilityAndMedicine(@Param("facilityId") Long facilityId, @Param("medicineId") Long medicineId);

    Optional<Prediction> findTopByFacilityIdAndMedicineIdOrderByCreatedAtDesc(Long facilityId, Long medicineId);

    List<Prediction> findByForecastDate(LocalDate forecastDate);
}