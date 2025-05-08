package com.awbd.awbd.controller;

import com.awbd.awbd.dto.VehicleCreationDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Adds a new vehicle to a client
     * 
     * @param clientId the ID of the client to add the vehicle to
     * @param vehicleCreationDto the vehicle data
     * @return the created vehicle as a DTO
     */
    @PostMapping("/client/{clientId}")
    public ResponseEntity<VehicleDto> addVehicleToClient(
            @PathVariable Long clientId,
            @RequestBody VehicleCreationDto vehicleCreationDto) {
        try {
            VehicleDto vehicleDto = vehicleService.addVehicleToClient(clientId, vehicleCreationDto);
            return new ResponseEntity<>(vehicleDto, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an existing vehicle if it belongs to the specified client
     * 
     * @param vehicleId the ID of the vehicle to update
     * @param clientId the ID of the client who owns the vehicle
     * @param vehicleCreationDto the updated vehicle data
     * @return the updated vehicle as a DTO
     */
    @PutMapping("/client/{clientId}/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long clientId,
            @RequestBody VehicleCreationDto vehicleCreationDto) {
        try {
            VehicleDto vehicleDto = vehicleService.updateVehicle(vehicleId, clientId, vehicleCreationDto);
            return new ResponseEntity<>(vehicleDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a vehicle if it belongs to the specified client
     * 
     * @param vehicleId the ID of the vehicle to delete
     * @param clientId the ID of the client who owns the vehicle
     * @return no content if successful, not found if the vehicle doesn't exist or doesn't belong to the client
     */
    @DeleteMapping("/clientId/{clientId}/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long clientId) {
        try {
            vehicleService.deleteVehicle(vehicleId, clientId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
