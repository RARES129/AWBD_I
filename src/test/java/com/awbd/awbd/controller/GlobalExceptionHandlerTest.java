package com.awbd.awbd.controller;

import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.IllegalArgumentWithViewException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleNotFoundException() {
        Exception ex = new ResourceNotFoundException("Resource not found");
        ModelAndView modelAndView = exceptionHandler.handlerNotFoundException(ex);

        assertEquals("notFoundException", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("exception"));
        assertEquals(ex, modelAndView.getModel().get("exception"));
    }

    @Test
    void testHandleEntityInUse_Vehicle() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        EntityInUnfinishedAppointmentException ex =
                new EntityInUnfinishedAppointmentException("Vehicle");

        String result = exceptionHandler.handleEntityInUse(ex, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMessage", "Vehicle is used in an unfinished appointment.");
        assertEquals("redirect:/vehicle", result);
    }

    @Test
    void testHandleEntityInUse_ServiceType() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        EntityInUnfinishedAppointmentException ex =
                new EntityInUnfinishedAppointmentException("Service Type");

        String result = exceptionHandler.handleEntityInUse(ex, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errorMessage", "Service Type is used in an unfinished appointment.");
        assertEquals("redirect:/service-type", result);
    }

    @Test
    void testHandleEntityInUse_UnknownEntity() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        EntityInUnfinishedAppointmentException ex =
                new EntityInUnfinishedAppointmentException("Other");

        String result = exceptionHandler.handleEntityInUse(ex, redirectAttributes);
        assertEquals("redirect:/", result);
    }

    @Test
    void testHandleIllegalArgumentWithView() {
        Model model = mock(Model.class);
        IllegalArgumentWithViewException ex = new IllegalArgumentWithViewException(
                "Invalid input",
                "attribute",
                "value",
                "customView"
        );

        String result = exceptionHandler.handleIllegalArgumentWithView(ex, model);

        verify(model).addAttribute("errorMessage", "Invalid input");
        verify(model).addAttribute("attribute", "value");
        assertEquals("customView", result);
    }
}
