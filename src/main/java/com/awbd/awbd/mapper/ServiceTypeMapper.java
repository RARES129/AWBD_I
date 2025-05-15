package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ServiceTypeCreationDto;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.repository.MechanicRepository;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    @Mapping(target = "mechanic", expression = "java(mechanic)")
    ServiceType toServiceType(ServiceTypeDto serviceTypeDto, @Context Mechanic mechanic);
    ServiceTypeDto toServiceTypeDto(ServiceType serviceType);

    void updateServiceTypeFromDto(ServiceTypeDto dto, @MappingTarget ServiceType serviceType);
}