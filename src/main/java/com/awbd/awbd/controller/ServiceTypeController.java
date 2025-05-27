package com.awbd.awbd.controller;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.service.ServiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @RequestMapping("/form")
    public String serviceTypeForm(Model model) {
        ServiceTypeDto serviceType = new ServiceTypeDto();
        model.addAttribute("serviceType",  serviceType);
        return "serviceTypeForm";
    }

    @PostMapping("")
    public String saveOrUpdate(@Valid @ModelAttribute("serviceType") ServiceTypeDto serviceType,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "serviceTypeForm";
        }
        serviceTypeService.save(serviceType);
        return "redirect:/service-type";
    }

    @RequestMapping("")
    public String serviceTypeList(Model model) {
        List<ServiceTypeDto> serviceTypes = serviceTypeService.findMechanicServiceTypes();
        model.addAttribute("serviceTypes",serviceTypes);
        return "serviceTypeList";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        ServiceTypeDto serviceType = serviceTypeService.findById(Long.valueOf(id));
        model.addAttribute("serviceType", serviceType);
        return "serviceTypeForm";
    }

    @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable String id){
        serviceTypeService.deleteById(Long.valueOf(id));
        return "redirect:/service-type";
    }
}