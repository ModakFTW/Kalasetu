package com.kalasetu.controller;

import com.kalasetu.repository.CategoryRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CategoryRepository categoryRepository;

    public GlobalControllerAdvice(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("allCategories", categoryRepository.findAll());
    }
}
