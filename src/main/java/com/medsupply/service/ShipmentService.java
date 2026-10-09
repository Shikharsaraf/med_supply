package com.medsupply.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medsupply.dto.ShipmentDTO;
import com.medsupply.exception.InvalidTransferException;
import com.medsupply.exception.ResourceNotFoundException;
import com.medsupply.model.Facility;
import com.medsupply.model.Medicine;
import com.medsupply.model.Shipment;
import com.medsupply.model.ShipmentStatus;
import com.medsupply.repository.FacilityRepository;
import com.medsupply.repository.MedicineRepository;
import com.medsupply.repository.ShipmentRepository;

@Service
@Transactional
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final FacilityRepository facilityRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryService inventoryService;

    public ShipmentService(ShipmentRepository shipmentRepository,
                           FacilityRepository facilityRepository,
                           MedicineRepository medicineRepository,
                           InventoryService inventoryService) {
        this.shipmentRepository = shipmentRepository;
        this.facilityRepository = facilityRepository;
        this.medicineRepository = medicineRepository;
        this.inventoryService = inventoryService;
    }

    public ShipmentDTO createShipment(ShipmentDTO dto) {
        if (dto.getSourceFacilityId().equals(dto.getDestinationFacilityId())) {
            throw new InvalidTransferException("Source and destination facilities must be different.");
        }

        Facility source = facilityRepository.findById(dto.getSourceFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Source facility not found with id: " + dto.getSourceFacilityId()));

        Facility destination = facilityRepository.findById(dto.getDestinationFacilityId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination facility not found with id: " + dto.getDestinationFacilityId()));

        Medicine medicine = medicineRepository.findById(dto.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + dto.getMedicineId()));

        Shipment shipment = new Shipment(
                null,
                source,
                destination,
                medicine,
                dto.getQuantity(),
                ShipmentStatus.REQUESTED,
                dto.getReason()
        );

        Shipment saved = shipmentRepository.save(shipment);
        return mapToDTO(saved);
    }

    public ShipmentDTO dispatchShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        if (shipment.getStatus() != ShipmentStatus.REQUESTED) {
            throw new InvalidTransferException("Only shipments with status REQUESTED can be dispatched. Current: " + shipment.getStatus());
        }

        // Deduct quantity from source inventory upon dispatch
        inventoryService.deductStock(
                shipment.getSourceFacility().getId(),
                shipment.getMedicine().getId(),
                shipment.getQuantity()
        );

        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        shipment.setDispatchedAt(LocalDateTime.now());

        Shipment updated = shipmentRepository.save(shipment);
        return mapToDTO(updated);
    }

    public ShipmentDTO deliverShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        if (shipment.getStatus() != ShipmentStatus.IN_TRANSIT) {
            throw new InvalidTransferException("Only shipments with status IN_TRANSIT can be delivered. Current: " + shipment.getStatus());
        }

        // Add quantity to destination facility upon delivery
        inventoryService.addStock(
                shipment.getDestinationFacility().getId(),
                shipment.getMedicine().getId(),
                shipment.getQuantity()
        );

        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipment.setDeliveredAt(LocalDateTime.now());

        Shipment updated = shipmentRepository.save(shipment);
        return mapToDTO(updated);
    }

    public ShipmentDTO cancelShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
            throw new InvalidTransferException("Delivered shipments cannot be cancelled.");
        }

        // If it was already dispatched, return the reserved stock back to the source facility
        if (shipment.getStatus() == ShipmentStatus.IN_TRANSIT) {
            inventoryService.addStock(
                    shipment.getSourceFacility().getId(),
                    shipment.getMedicine().getId(),
                    shipment.getQuantity()
            );
        }

        shipment.setStatus(ShipmentStatus.CANCELLED);
        Shipment updated = shipmentRepository.save(shipment);
        return mapToDTO(updated);
    }

    @Transactional(readOnly = true)
    public ShipmentDTO getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        return mapToDTO(shipment);
    }

    @Transactional(readOnly = true)
    public List<ShipmentDTO> getShipmentsByDestination(Long destinationFacilityId) {
        return shipmentRepository.findByDestinationFacilityId(destinationFacilityId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShipmentDTO> getShipmentsByStatus(ShipmentStatus status) {
        return shipmentRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShipmentDTO mapToDTO(Shipment shipment) {
        return new ShipmentDTO(
                shipment.getId(),
                shipment.getSourceFacility().getId(),
                shipment.getSourceFacility().getName(),
                shipment.getDestinationFacility().getId(),
                shipment.getDestinationFacility().getName(),
                shipment.getMedicine().getId(),
                shipment.getMedicine().getName(),
                shipment.getQuantity(),
                shipment.getStatus(),
                shipment.getReason(),
                shipment.getCreatedAt(),
                shipment.getDispatchedAt(),
                shipment.getDeliveredAt()
        );
    }
}