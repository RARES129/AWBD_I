package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.VehicleRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    Logger log = LoggerFactory.getLogger(AppointmentMapper.class);

    @Mapping(target = "id", source = "appointmentDto.id")
    @Mapping(target = "client", expression = "java(client)")
    @Mapping(target = "mechanic", source = "appointmentDto.mechanicId", qualifiedByName = "mapMechanic")
    @Mapping(target = "vehicle", source = "appointmentDto.vehicleId", qualifiedByName = "mapVehicle")
    @Mapping(target = "serviceTypes", source = "appointmentDto.serviceTypeIds", qualifiedByName = "mapServiceTypes")
    Appointment toAppointment(AppointmentDto appointmentDto,
                              Client client,
                              @Context MechanicRepository mechanicRepository,
                              @Context VehicleRepository vehicleRepository,
                              @Context ServiceTypeRepository serviceTypeRepository);

    @Named("mapMechanic")
    default Mechanic mapMechanic(Long mechanicId, @Context MechanicRepository mechanicRepository) {
        log.info("   Mapping mechanic with ID: {}", mechanicId);
        return mechanicId != null ? mechanicRepository.findById(mechanicId).orElse(null) : null;
    }

    @Named("mapVehicle")
    default Vehicle mapVehicle(Long vehicleId, @Context VehicleRepository vehicleRepository) {
        log.info("   Mapping vehicle with ID: {}", vehicleId);
        return vehicleId != null ? vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + vehicleId)) : null;
    }

    @Named("mapServiceTypes")
    default List<ServiceType> mapServiceTypes(List<Long> serviceTypeIds, @Context ServiceTypeRepository serviceTypeRepository) {
        log.info("   Mapping service types with IDs: {}", serviceTypeIds);
        return serviceTypeIds != null ?
                serviceTypeIds.stream()
                        .map(serviceTypeId -> {
                            log.info("      Mapping service type with ID: {}", serviceTypeId);
                            return serviceTypeId != null ? serviceTypeRepository.findById(serviceTypeId).orElse(null) : null;
                        })
                        .toList()
                : null;    }
}