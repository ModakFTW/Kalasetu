package com.kalasetu.controller;

import com.kalasetu.model.Product;
import com.kalasetu.model.User;
import com.kalasetu.model.CustomerOrder;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.ProductRepository;
import com.kalasetu.repository.CustomerOrderRepository;
import com.kalasetu.repository.UserRepository;
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
    private final CustomerOrderRepository customerOrderRepository;
    private final UserRepository userRepository;

    public StorefrontController(ProductRepository productRepository,
                                ArtistRepository artistRepository,
                                CustomerOrderRepository customerOrderRepository,
                                UserRepository userRepository) {
        this.productRepository = productRepository;
        this.artistRepository = artistRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.userRepository = userRepository;
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
    public String checkout(@RequestParam("productId") Long productId, Model model) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            model.addAttribute("productId", productId);
            return "checkout";
        }
        return "redirect:/store";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam("productId") Long productId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        Long userId = sessionUser.getId();

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> prodOpt = productRepository.findById(productId);

        if (userOpt.isPresent() && prodOpt.isPresent()) {
            Product product = prodOpt.get();
            
            // Check inventory
            if (product.getIsAvailable() != null && product.getIsAvailable() && product.getInventoryCount() != null && product.getInventoryCount() > 0) {
                // Decrement inventory
                product.setInventoryCount(product.getInventoryCount() - 1);
                if (product.getInventoryCount() == 0) {
                    product.setIsAvailable(false);
                }
                productRepository.save(product);

                CustomerOrder order = new CustomerOrder();
                order.setCustomer(userOpt.get());
                order.setProduct(product);
                order.setOrderDate(java.time.LocalDateTime.now());
                order.setStatus("PLACED");
                customerOrderRepository.save(order);
                
                return "redirect:/profile?orderSuccess";
            } else {
                return "redirect:/store?error=out_of_stock";
            }
        }

        return "redirect:/profile?orderSuccess";
    }

    @GetMapping("/profile")
    public String customerProfile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        Long userId = sessionUser.getId();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("customer", user);
            model.addAttribute("orders", customerOrderRepository.findByCustomerOrderByOrderDateDesc(user));
            return "customer-profile";
        }
        return "redirect:/";
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
