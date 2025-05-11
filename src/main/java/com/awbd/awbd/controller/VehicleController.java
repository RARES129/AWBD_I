package com.awbd.awbd.controller;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @RequestMapping("/form")
    public String vehicleForm(Model model) {
        VehicleDto vehicle = new VehicleDto();
        model.addAttribute("vehicle",  vehicle);
        return "vehicleForm";
    }

    @PostMapping("")
    public String saveOrUpdate(@ModelAttribute VehicleDto vehicle) {
        vehicleService.save(vehicle);
        return "redirect:/vehicle" ;
    }

    @RequestMapping("")
    public String vehicleList(Model model) {
        List<VehicleDto> vehicles = vehicleService.findClientVehicles();
        model.addAttribute("vehicles",vehicles);
        return "vehicleList";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("vehicle", vehicleService.findById(Long.valueOf(id)));
        System.out.println(vehicleService.findById(Long.valueOf(id)));
        return "vehicleForm";
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable String id){
        vehicleService.deleteById(Long.valueOf(id));
        return "redirect:/vehicle";
    }
}
