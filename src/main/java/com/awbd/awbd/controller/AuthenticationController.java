package com.awbd.awbd.controller;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user")UserDto user,
                           BindingResult result, Model model) {
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