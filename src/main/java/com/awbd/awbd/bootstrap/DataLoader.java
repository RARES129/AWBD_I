package com.awbd.awbd.bootstrap;

import com.awbd.awbd.entity.Admin;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("postgres")
public class DataLoader implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    private void loadUserData() {
        if (userRepository.count() == 0){

            Admin admin = Admin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("12345"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }
}
