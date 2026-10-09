package com.medsupply.service;

import com.medsupply.dto.RebalanceRecommendationDTO;
import com.medsupply.dto.ShipmentDTO;
import com.medsupply.model.Facility;
import com.medsupply.model.Inventory;
import com.medsupply.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RebalancingService {

    private final InventoryRepository inventoryRepository;
    private final ShipmentService shipmentService;

    public RebalancingService(InventoryRepository inventoryRepository, ShipmentService shipmentService) {
        this.inventoryRepository = inventoryRepository;
        this.shipmentService = shipmentService;
    }

    public List<RebalanceRecommendationDTO> executeAutoRebalance(boolean autoCreateShipment) {
        List<Inventory> deficits = inventoryRepository.findBelowMinimumLevel();
        List<RebalanceRecommendationDTO> recommendations = new ArrayList<>();

        for (Inventory targetInv : deficits) {
            Long medicineId = targetInv.getMedicine().getId();
            Facility targetFacility = targetInv.getFacility();
            int requiredQuantity = targetInv.getMinimumLevel() - targetInv.getQuantity();

            if (requiredQuantity <= 0) {
                continue;
            }

            // Find facilities with available surplus of this medicine
            List<Inventory> candidateDonors = inventoryRepository.findByMedicineId(medicineId).stream()
                    .filter(inv -> !inv.getFacility().getId().equals(targetFacility.getId()))
                    .filter(inv -> (inv.getQuantity() - inv.getMinimumLevel()) > 50) // Donor must have surplus above minimum
                    .sorted(Comparator.comparingDouble(inv -> calculateHaversineDistance(
                            targetFacility.getLatitude(), targetFacility.getLongitude(),
                            inv.getFacility().getLatitude(), inv.getFacility().getLongitude()
                    )))
                    .toList();

            if (!candidateDonors.isEmpty()) {
                Inventory bestDonor = candidateDonors.get(0);
                int availableSurplus = bestDonor.getQuantity() - bestDonor.getMinimumLevel();
                int transferQty = Math.min(requiredQuantity, availableSurplus);

                double distance = calculateHaversineDistance(
                        bestDonor.getFacility().getLatitude(), bestDonor.getFacility().getLongitude(),
                        targetFacility.getLatitude(), targetFacility.getLongitude()
                );

                Long shipmentId = null;
                if (autoCreateShipment) {
                    ShipmentDTO request = new ShipmentDTO();
                    request.setSourceFacilityId(bestDonor.getFacility().getId());
                    request.setDestinationFacilityId(targetFacility.getId());
                    request.setMedicineId(medicineId);
                    request.setQuantity(transferQty);
                    request.setReason("Automated rebalance to resolve stock deficit below minimum threshold");

                    ShipmentDTO created = shipmentService.createShipment(request);
                    shipmentId = created.getId();
                }

                recommendations.add(new RebalanceRecommendationDTO(
                        medicineId,
                        targetInv.getMedicine().getName(),
                        bestDonor.getFacility().getId(),
                        bestDonor.getFacility().getName(),
                        targetFacility.getId(),
                        targetFacility.getName(),
                        transferQty,
                        Math.round(distance * 10.0) / 10.0,
                        shipmentId
                ));
            }
        }

        return recommendations;
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}