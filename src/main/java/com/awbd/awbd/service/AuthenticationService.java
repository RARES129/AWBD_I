package com.awbd.awbd.service;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.exceptions.IllegalArgumentWithViewException;
import com.awbd.awbd.mapper.UserMapper;
import com.awbd.awbd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentWithViewException(
                    "Username is already taken",
                    "user",
                    userDto,
                    "register");
        }

        User user = switch (userDto.getRole()) {
            case CLIENT -> new Client();
            case MECHANIC -> new Mechanic();
            case ADMIN -> throw new IllegalArgumentException("Invalid role");
        };

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userMapper.updateUserFromRequest(userDto, user);
        userRepository.save(user);
    }
}
