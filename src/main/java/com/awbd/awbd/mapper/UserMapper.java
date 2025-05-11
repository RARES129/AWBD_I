package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ClientDto;
import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.dto.RegisterRequestBody;
import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void updateUserFromRequest(UserDto userDto, @MappingTarget User user);

//    @SubclassMapping(source = ClientDto.class, target = Client.class)
//    @SubclassMapping(source = MechanicDto.class, target = Mechanic.class)
//    User toUser(RegisterRequestBody registerRequestBody);
//    @SubclassMapping(source = Client.class, target = ClientDto.class)
//    @SubclassMapping(source = Mechanic.class, target = MechanicDto.class)
//    UserDto ToUserDTO(User User);
}


