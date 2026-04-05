package com.kalasetu.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalErrorController {

    // Only catch genuine 404s from missing controller routes (not missing static files)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
        return "error";
    }
}
