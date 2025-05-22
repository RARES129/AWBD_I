package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    @Mappings({
            @Mapping(source = "client.id", target = "clientId"),
            @Mapping(source = "client.username", target = "clientUsername"),
            @Mapping(source = "mechanic.id", target = "mechanicId"),
            @Mapping(source = "mechanic.username", target = "mechanicUsername"),
            @Mapping(source = "appointment.id", target = "appointmentId"),
            @Mapping(source = "appointment.dateTime", target = "appointmentDateTime"),
    })
    ReceiptDto toReceiptDto(Receipt receipt);
}