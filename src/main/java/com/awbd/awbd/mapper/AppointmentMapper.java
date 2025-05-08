package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.AppointmentCreationDto;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "mechanic.id", source = "mechanicId")
    @Mapping(target = "vehicle.id", source = "vehicleId")
    @Mapping(target = "serviceTypes", ignore = true)
    Appointment toAppointment(AppointmentCreationDto appointmentCreationDto);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "mechanicId", source = "mechanic.id")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "serviceTypeIds", expression = "java(appointment.getServiceTypes().stream().map(serviceType -> serviceType.getId()).toList())")
    AppointmentDto toAppointmentDto(Appointment appointment);
}