package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ClientDto;
import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.dto.RegisterRequestBody;
import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @SubclassMapping(source = ClientDto.class, target = Client.class)
//    @SubclassMapping(source = MechanicDto.class, target = Mechanic.class)
//    User toUser(UserDto userDto);
    @SubclassMapping(source = Client.class, target = ClientDto.class)
    @SubclassMapping(source = Mechanic.class, target = MechanicDto.class)
    UserDto ToUserDTO(User User);
}


