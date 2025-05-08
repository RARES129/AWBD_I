package com.awbd.awbd.service;

import com.awbd.awbd.dto.VehicleCreationDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.UserRepository;
import com.awbd.awbd.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;

    /**
     * Adds a new vehicle to a client
     * 
     * @param clientId the ID of the client to add the vehicle to
     * @param vehicleCreationDto the vehicle data
     * @return the created vehicle as a DTO
     * @throws NoSuchElementException if the client is not found
     */
    @Transactional
    public VehicleDto addVehicleToClient(Long clientId, VehicleCreationDto vehicleCreationDto) {
        System.out.println(vehicleCreationDto);
        System.out.println(clientId);
        Client client = (Client) userRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client not found with ID: " + clientId));

        Vehicle vehicle = vehicleMapper.toVehicle(vehicleCreationDto);
        vehicle.setOwner(client);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    /**
     * Updates an existing vehicle if it belongs to the specified client
     * 
     * @param vehicleId the ID of the vehicle to update
     * @param clientId the ID of the client who owns the vehicle
     * @param vehicleCreationDto the updated vehicle data
     * @return the updated vehicle as a DTO
     * @throws NoSuchElementException if the vehicle is not found or doesn't belong to the client
     */
    @Transactional
    public VehicleDto updateVehicle(Long vehicleId, Long clientId, VehicleCreationDto vehicleCreationDto) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found with ID: " + vehicleId));

        // Check if the vehicle belongs to the specified client
        if (existingVehicle.getOwner() == null || !existingVehicle.getOwner().getId().equals(clientId)) {
            throw new NoSuchElementException("Vehicle with ID: " + vehicleId + " does not belong to client with ID: " + clientId);
        }

        // Update the vehicle properties
        existingVehicle.setBrand(vehicleCreationDto.getBrand());
        existingVehicle.setModel(vehicleCreationDto.getModel());
        existingVehicle.setPlateNumber(vehicleCreationDto.getPlateNumber());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return vehicleMapper.toVehicleDto(updatedVehicle);
    }

    /**
     * Deletes a vehicle if it belongs to the specified client
     * 
     * @param vehicleId the ID of the vehicle to delete
     * @param clientId the ID of the client who owns the vehicle
     * @throws NoSuchElementException if the vehicle is not found or doesn't belong to the client
     */
    @Transactional
    public void deleteVehicle(Long vehicleId, Long clientId) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found with ID: " + vehicleId));

        // Check if the vehicle belongs to the specified client
        if (existingVehicle.getOwner() == null || !existingVehicle.getOwner().getId().equals(clientId)) {
            throw new NoSuchElementException("Vehicle with ID: " + vehicleId + " does not belong to client with ID: " + clientId);
        }

        vehicleRepository.deleteById(vehicleId);
    }
}
