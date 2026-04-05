package com.kalasetu.controller;

import com.kalasetu.model.Product;
import com.kalasetu.model.User;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class StorefrontController {

    private final ProductRepository productRepository;
    private final ArtistRepository artistRepository;

    public StorefrontController(ProductRepository productRepository,
                                ArtistRepository artistRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "home";
    }

    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "store";
    }

    // Support both /product/{id} and /products/{id} to avoid any 404s from old links
    @GetMapping({"/product/{id}", "/products/{id}"})
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-detail";
        }
        return "redirect:/store";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout() {
        return "redirect:/store?orderSuccess";
    }

    @GetMapping("/our-story")
    public String ourStory(Model model) {
        return "our-story";
    }

    @GetMapping("/artists")
    public String artists(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        return "artists";
    }
}
