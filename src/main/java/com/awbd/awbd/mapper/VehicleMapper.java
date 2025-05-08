package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.VehicleCreationDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toVehicle(VehicleCreationDto vehicleCreationDto);
    VehicleDto toVehicleDto(Vehicle vehicle);
}
