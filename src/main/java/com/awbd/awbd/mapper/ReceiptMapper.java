package com.awbd.awbd.mapper;


import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {


    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "mechanicId", source = "mechanic.id")
    @Mapping(target = "appointmentId", source = "appointment.id")
    ReceiptDto toReceiptDto(Receipt receipt);
}