package com.kalasetu.controller;

import com.kalasetu.model.Artist;
import com.kalasetu.model.User;
import com.kalasetu.model.UserRole;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.ProductRepository;
import com.kalasetu.repository.UserRepository;
import com.kalasetu.repository.ComplaintRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ArtistRepository artistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;

    public AdminController(ArtistRepository artistRepository, 
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           ComplaintRepository complaintRepository) {
        this.artistRepository = artistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("pendingArtists", artistRepository.findByApprovedFalse());
        model.addAttribute("totalArtists", artistRepository.count());
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("pendingComplaints", complaintRepository.findAllByOrderByCreatedAtDesc().stream().filter(c -> "PENDING".equals(c.getStatus())).count());
        
        return "admin/dashboard";
    }

    @GetMapping("/artists/verify")
    public String verifyArtists(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("artists", artistRepository.findByApprovedFalse());
        return "admin/verify-artists";
    }

    @PostMapping("/artists/approve/{id}")
    public String approveArtist(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setApproved(true);
            artistRepository.save(artist);
        }
        return "redirect:/admin/artists/verify?success=artist_approved";
    }

    @PostMapping("/artists/reject/{id}")
    public String rejectArtist(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        artistRepository.deleteById(id);
        return "redirect:/admin/artists/verify?success=artist_rejected";
    }

    @GetMapping("/products/monitor")
    public String monitorProducts(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("products", productRepository.findAll());
        return "admin/monitor-products";
    }
    
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        productRepository.deleteById(id);
        return "redirect:/admin/products/monitor?success=product_deleted";
    }

    @GetMapping("/complaints")
    public String listComplaints(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        
        model.addAttribute("complaints", complaintRepository.findAllByOrderByCreatedAtDesc());
        return "admin/complaints";
    }

    @PostMapping("/complaints/resolve/{id}")
    public String resolveComplaint(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        complaintRepository.findById(id).ifPresent(c -> {
            c.setStatus("RESOLVED");
            complaintRepository.save(c);
        });
        return "redirect:/admin/complaints?success=resolved";
    }

    @PostMapping("/complaints/dismiss/{id}")
    public String dismissComplaint(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        
        complaintRepository.findById(id).ifPresent(c -> {
            c.setStatus("DISMISSED");
            complaintRepository.save(c);
        });
        return "redirect:/admin/complaints?success=dismissed";
    }
}
