package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "owner", expression = "java(client)")
    Vehicle toVehicle(VehicleDto vehicleDto,
                      @Context Client client);
    VehicleDto toVehicleDto(Vehicle vehicle);
    void updateVehicleFromDto(VehicleDto dto, @MappingTarget Vehicle vehicle);
}
