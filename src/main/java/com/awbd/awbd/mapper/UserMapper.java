package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.UserCreationRequestDto;
import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequestDto userCreationRequestDto);
    UserDto toUserDto(User user);
}
