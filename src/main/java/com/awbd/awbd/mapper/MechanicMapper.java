package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ClientDto;
import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.entity.Vehicle;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface MechanicMapper {
    MechanicDto toMechanicDto(Mechanic mechanic);
}
