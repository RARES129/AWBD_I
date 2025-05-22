package com.awbd.awbd.controller;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.service.MechanicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    private final MechanicService mechanicService;

    @RequestMapping("")
    public String mechanicList(Model model) {
        List<MechanicDto> mechanics = mechanicService.findAll();
        model.addAttribute("mechanics",mechanics);
        return "mechanicList";
    }
}
