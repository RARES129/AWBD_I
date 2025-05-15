package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.mapper.ServiceTypeMapper;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;
    private final MechanicRepository mechanicRepository;

    @Transactional
    public void save(ServiceTypeDto serviceTypeDto) {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        ServiceType serviceType;

        if (serviceTypeDto.getId() != null) {
           serviceType = serviceTypeRepository.findById(serviceTypeDto.getId())
                   .orElseThrow(() -> new RuntimeException("Service Type not found."));
           serviceTypeMapper.updateServiceTypeFromDto(serviceTypeDto, serviceType);
        } else {
            serviceType = serviceTypeMapper.toServiceType(serviceTypeDto, mechanic);
            mechanic.getServiceTypes().add(serviceType);
        }

        serviceTypeRepository.save(serviceType);
    }

    public List<ServiceTypeDto> findMechanicServiceTypes() {
        String username = SecurityUtil.getSessionUsername();
        Mechanic mechanic = mechanicRepository.findByUsername(username);

        return serviceTypeRepository.findByMechanicId(mechanic.getId())
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .toList();
    }

    public List<ServiceTypeDto> findMechanicServiceTypes(Long mechanicId) {
        return serviceTypeRepository.findByMechanicId(mechanicId)
                .stream()
                .map(serviceTypeMapper::toServiceTypeDto)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        serviceTypeRepository.deleteById(id);
    }

//    /**
//     * Creates a new service type for a mechanic
//     *
//     * @param mechanicId the ID of the mechanic to add the service type to
//     * @param serviceTypeCreationDto the service type data
//     * @return the created service type as a DTO
//     * @throws NoSuchElementException if the mechanic is not found
//     */
//    @Transactional
//    public ServiceTypeDto createServiceType(Long mechanicId, ServiceTypeCreationDto serviceTypeCreationDto) {
//        Mechanic mechanic = mechanicRepository.findById(mechanicId)
//                .orElseThrow(() -> new NoSuchElementException("Mechanic not found with ID: " + mechanicId));
//
//        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeCreationDto);
//        ServiceType savedServiceType = serviceTypeRepository.save(serviceType);
//
//        // Add the service type to the mechanic's list of service types
////        mechanic.getServiceTypes().add(savedServiceType);
//        mechanicRepository.save(mechanic);
//
//        return serviceTypeMapper.toServiceTypeDto(savedServiceType);
//    }
//
//    /**
//     * Gets all service types for a mechanic
//     *
//     * @param mechanicId the ID of the mechanic
//     * @return a list of service types as DTOs
//     * @throws NoSuchElementException if the mechanic is not found
//     */
//    @Transactional(readOnly = true)
//    public List<ServiceTypeDto> getServiceTypesByMechanic(Long mechanicId) {
//        Mechanic mechanic = mechanicRepository.findById(mechanicId)
//                .orElseThrow(() -> new NoSuchElementException("Mechanic not found with ID: " + mechanicId));
//
//        return new ArrayList<>();
//
////        return mechanic.getServiceTypes().stream()
////                .map(serviceTypeMapper::toServiceTypeDto)
////                .collect(Collectors.toList());
//    }
//
//    /**
//     * Updates an existing service type if it belongs to the specified mechanic
//     *
//     * @param serviceTypeId the ID of the service type to update
//     * @param mechanicId the ID of the mechanic who owns the service type
//     * @param serviceTypeCreationDto the updated service type data
//     * @return the updated service type as a DTO
//     * @throws NoSuchElementException if the service type is not found or doesn't belong to the mechanic
//     */
//    @Transactional
//    public ServiceTypeDto updateServiceType(Long serviceTypeId, Long mechanicId, ServiceTypeCreationDto serviceTypeCreationDto) {
//        ServiceType existingServiceType = serviceTypeRepository.findById(serviceTypeId)
//                .orElseThrow(() -> new NoSuchElementException("Service type not found with ID: " + serviceTypeId));
//
//        Mechanic mechanic = mechanicRepository.findById(mechanicId)
//                .orElseThrow(() -> new NoSuchElementException("Mechanic not found with ID: " + mechanicId));
//
//        // Check if the service type belongs to the specified mechanic
////        if (!mechanic.getServiceTypes().contains(existingServiceType)) {
////            throw new NoSuchElementException("Service type with ID: " + serviceTypeId + " does not belong to mechanic with ID: " + mechanicId);
////        }
//
//        // Update the service type properties
//        existingServiceType.setName(serviceTypeCreationDto.getName());
//        existingServiceType.setPrice(serviceTypeCreationDto.getPrice());
//
//        ServiceType updatedServiceType = serviceTypeRepository.save(existingServiceType);
//        return serviceTypeMapper.toServiceTypeDto(updatedServiceType);
//    }
//
//    /**
//     * Deletes a service type if it belongs to the specified mechanic
//     *
//     * @param serviceTypeId the ID of the service type to delete
//     * @param mechanicId the ID of the mechanic who owns the service type
//     * @throws NoSuchElementException if the service type is not found or doesn't belong to the mechanic
//     */
//    @Transactional
//    public void deleteServiceType(Long serviceTypeId, Long mechanicId) {
//        ServiceType existingServiceType = serviceTypeRepository.findById(serviceTypeId)
//                .orElseThrow(() -> new NoSuchElementException("Service type not found with ID: " + serviceTypeId));
//
//        Mechanic mechanic = mechanicRepository.findById(mechanicId)
//                .orElseThrow(() -> new NoSuchElementException("Mechanic not found with ID: " + mechanicId));
//
//        // Check if the service type belongs to the specified mechanic
////        if (!mechanic.getServiceTypes().contains(existingServiceType)) {
////            throw new NoSuchElementException("Service type with ID: " + serviceTypeId + " does not belong to mechanic with ID: " + mechanicId);
////        }
//
//        // Remove the service type from the mechanic's list of service types
////        mechanic.getServiceTypes().remove(existingServiceType);
//        mechanicRepository.save(mechanic);
//
//        serviceTypeRepository.deleteById(serviceTypeId);
//    }
}