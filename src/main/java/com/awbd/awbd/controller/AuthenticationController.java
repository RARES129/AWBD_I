package com.awbd.awbd.controller;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.service.JpaUserDetailsService;
import com.awbd.awbd.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor

public class AuthenticationController {

    private final JpaUserDetailsService userDetailsService;

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register.html";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")UserDto user,
                           BindingResult result, Model model) {
        var existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername.isPresent() && existingUserUsername.get().getUsername() != null && !existingUserUsername.get().getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "/register";
        }
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogInForm(){ return "login"; }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "accessDenied"; }

//    private final UserService userService;
//
//    @PostMapping
//
//    public ResponseEntity<String> register(
//            @Valid @RequestBody
//            RegisterRequestBody registerRequestBody) {
//        User user = userService.createUser(registerRequestBody);
//
//        return new ResponseEntity<>("user created succesfully", HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        System.out.println("users: " + users);
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(
//            @PathVariable Long id) {
//        Optional<User> user = userService.getUserById(id);
//        if (user.isPresent()) {
//            return new ResponseEntity<>(user.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(
//            @PathVariable Long id) {
//        try {
//            userService.deleteUser(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}