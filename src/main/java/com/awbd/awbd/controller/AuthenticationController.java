package com.awbd.awbd.controller;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register.html";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")UserDto user,
                           BindingResult result, Model model) {
        User existingUser = authenticationService.findByUsername(user.getUsername());
        if(existingUser != null) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "/register";
        }
        authenticationService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogInForm(){ return "login"; }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "accessDenied"; }
}