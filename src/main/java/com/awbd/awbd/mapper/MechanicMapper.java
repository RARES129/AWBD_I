package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.entity.Mechanic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MechanicMapper {
    MechanicDto toMechanicDto(Mechanic mechanic);
}
