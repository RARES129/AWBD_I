package com.awbd.awbd.controller;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.service.ServiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String serviceTypeList(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        Page<ServiceTypeDto> serviceTypePage = serviceTypeService.findMechanicServiceTypesPaginated(PageRequest.of(page, size, Sort.by(sortBy)));
        model.addAttribute("serviceTypePage",serviceTypePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
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