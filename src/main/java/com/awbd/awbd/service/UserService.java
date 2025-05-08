package com.awbd.awbd.service;

import com.awbd.awbd.dto.RegisterRequestBody;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.mapper.UserMapper;
import com.awbd.awbd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(RegisterRequestBody registerRequestBody) {
//        User user = userMapper.toUser(registerRequestBody);
//        return userRepository.save(user);
        User user = switch (registerRequestBody.getRole()) {
            case "CLIENT" -> new Client();
            case "MECHANIC" -> new Mechanic();
            default -> throw new IllegalArgumentException("Invalid role: " + registerRequestBody.getRole());
        };

        user.setUsername(registerRequestBody.getUsername());
        user.setPassword(registerRequestBody.getPassword());
        user.setRole(Role.valueOf(registerRequestBody.getRole()));

        System.out.println(user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
