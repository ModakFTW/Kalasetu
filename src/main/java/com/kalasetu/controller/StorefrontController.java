package com.kalasetu.controller;

import com.kalasetu.model.Product;
import com.kalasetu.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class StorefrontController {

    private final ProductRepository productRepository;

    public StorefrontController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Landing page no longer needs products
        return "home";
    }

    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "store";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-detail";
        } else {
            return "redirect:/";
        }
    }
}
