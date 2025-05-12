package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.AppointmentCreationDto;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.VehicleRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
//    @Mapping(target = "client.id", source = "clientId")
//    @Mapping(target = "mechanic.id", source = "mechanicId")
//    @Mapping(target = "vehicle.id", source = "vehicleId")
//    @Mapping(target = "serviceTypes", ignore = true)
//    Appointment toAppointment(AppointmentCreationDto appointmentCreationDto);
//
//    @Mapping(target = "clientId", source = "client.id")
//    @Mapping(target = "mechanicId", source = "mechanic.id")
//    @Mapping(target = "vehicleId", source = "vehicle.id")
//    @Mapping(target = "serviceTypeIds", expression = "java(appointment.getServiceTypes().stream().map(serviceType -> serviceType.getId()).toList())")
//    AppointmentDto toAppointmentDto(Appointment appointment);

    @Mapping(target = "id", source = "appointmentDto.id")
    @Mapping(target = "client", expression = "java(client)")
    @Mapping(target = "mechanic", source = "appointmentDto.mechanicId", qualifiedByName = "mapMechanic")
    @Mapping(target = "vehicle", source = "appointmentDto.vehicleId", qualifiedByName = "mapVehicle")
    Appointment toAppointment(AppointmentDto appointmentDto,
                              Client client,
                              @Context MechanicRepository mechanicRepository,
                              @Context VehicleRepository vehicleRepository);

    @Named("mapMechanic")
    default Mechanic mapMechanic(Long mechanicId, @Context MechanicRepository mechanicRepository) {
        System.out.println("Mapping mechanic with ID: " + mechanicId);
        return mechanicId != null ? mechanicRepository.findById(mechanicId).orElse(null) : null;
    }

    @Named("mapVehicle")
    default Vehicle mapVehicle(Long vehicleId, @Context VehicleRepository vehicleRepository) {
        System.out.println("Mapping vehicle with ID: " + vehicleId);
        return vehicleId != null ? vehicleRepository.findById(vehicleId).orElse(null) : null;
    }
}