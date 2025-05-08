package com.awbd.awbd.controller;

import com.awbd.awbd.dto.ServiceTypeCreationDto;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    /**
     * Creates a new service type for a mechanic
     *
     * @param mechanicId the ID of the mechanic to add the service type to
     * @param serviceTypeCreationDto the service type data
     * @return the created service type as a DTO
     */
    @PostMapping("/mechanic/{mechanicId}")
    public ResponseEntity<ServiceTypeDto> createServiceType(
            @PathVariable Long mechanicId,
            @RequestBody ServiceTypeCreationDto serviceTypeCreationDto) {
        try {
            ServiceTypeDto serviceTypeDto = serviceTypeService.createServiceType(mechanicId, serviceTypeCreationDto);
            return new ResponseEntity<>(serviceTypeDto, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all service types for a mechanic
     *
     * @param mechanicId the ID of the mechanic
     * @return a list of service types as DTOs
     */
    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<List<ServiceTypeDto>> getServiceTypesByMechanic(@PathVariable Long mechanicId) {
        try {
            List<ServiceTypeDto> serviceTypeDtos = serviceTypeService.getServiceTypesByMechanic(mechanicId);
            return new ResponseEntity<>(serviceTypeDtos, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an existing service type if it belongs to the specified mechanic
     *
     * @param serviceTypeId the ID of the service type to update
     * @param mechanicId the ID of the mechanic who owns the service type
     * @param serviceTypeCreationDto the updated service type data
     * @return the updated service type as a DTO
     */
    @PutMapping("/mechanic/{mechanicId}/{serviceTypeId}")
    public ResponseEntity<ServiceTypeDto> updateServiceType(
            @PathVariable Long serviceTypeId,
            @PathVariable Long mechanicId,
            @RequestBody ServiceTypeCreationDto serviceTypeCreationDto) {
        try {
            ServiceTypeDto serviceTypeDto = serviceTypeService.updateServiceType(serviceTypeId, mechanicId, serviceTypeCreationDto);
            return new ResponseEntity<>(serviceTypeDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a service type if it belongs to the specified mechanic
     *
     * @param serviceTypeId the ID of the service type to delete
     * @param mechanicId the ID of the mechanic who owns the service type
     * @return no content if successful, not found if the service type doesn't exist or doesn't belong to the mechanic
     */
    @DeleteMapping("/mechanic/{mechanicId}/{serviceTypeId}")
    public ResponseEntity<Void> deleteServiceType(
            @PathVariable Long serviceTypeId,
            @PathVariable Long mechanicId) {
        try {
            serviceTypeService.deleteServiceType(serviceTypeId, mechanicId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}