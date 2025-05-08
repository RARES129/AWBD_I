package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ServiceTypeCreationDto;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.ServiceType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    ServiceType toServiceType(ServiceTypeCreationDto serviceTypeCreationDto);
    ServiceTypeDto toServiceTypeDto(ServiceType serviceType);
}