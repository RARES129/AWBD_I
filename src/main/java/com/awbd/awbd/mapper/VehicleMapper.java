package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "owner", expression = "java(client)")
    Vehicle toVehicle(VehicleDto vehicleDto,
                      @Context Client client);
    VehicleDto toVehicleDto(Vehicle vehicle);
}
