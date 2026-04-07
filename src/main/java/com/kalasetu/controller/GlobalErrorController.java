package com.kalasetu.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class GlobalErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest webRequest, Model model) {
        Map<String, Object> attrs = errorAttributes.getErrorAttributes(webRequest, 
            ErrorAttributeOptions.of(Include.MESSAGE, Include.BINDING_ERRORS));
        
        model.addAttribute("errorCode", attrs.getOrDefault("status", "500"));
        model.addAttribute("errorMessage", "Artisan System Error: " + attrs.getOrDefault("message", "No detailed message available."));
        
        return "error";
    }
}
