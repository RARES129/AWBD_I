package com.awbd.awbd.service;

import com.awbd.awbd.dto.UserCreationRequestDto;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.mapper.UserMapper;
import com.awbd.awbd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(UserCreationRequestDto userCreationRequestDto) {
        User user = userMapper.toUser(userCreationRequestDto);
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

