package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void updateUserFromRequest(UserDto userDto, @MappingTarget User user);
}


