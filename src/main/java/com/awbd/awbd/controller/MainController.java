package com.awbd.awbd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class MainController {


    @RequestMapping("")
    public String vehicleForm() {

        return "main";
    }



}