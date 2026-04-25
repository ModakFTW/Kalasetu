package com.kalasetu.controller;

import com.kalasetu.model.Artist;
import com.kalasetu.model.Commission;
import com.kalasetu.model.CommissionStatus;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.CommissionRepository;
import com.kalasetu.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/commission")
public class CommissionController {

    private final CommissionRepository commissionRepository;
    private final ArtistRepository artistRepository;
    private final ProductRepository productRepository;

    public CommissionController(CommissionRepository commissionRepository, 
                                ArtistRepository artistRepository, 
                                ProductRepository productRepository) {
        this.commissionRepository = commissionRepository;
        this.artistRepository = artistRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/request/{artistId}")
    public String showRequestForm(@PathVariable long artistId, HttpSession session, Model model) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get());
            
            Commission commission = new Commission();
            com.kalasetu.model.User user = (com.kalasetu.model.User) session.getAttribute("user");
            if (user != null) {
                commission.setRequesterName(user.getName());
                commission.setRequesterEmail(user.getEmail());
                commission.setRequesterContact(user.getPhoneNumber()); // Use phone as contact
            }
            
            model.addAttribute("commission", commission);
            return "commission-form";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/request")
    public String submitRequest(@RequestParam long artistId, @ModelAttribute Commission commission) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            commission.setArtist(artist.get());
            commission.setStatus(CommissionStatus.REQUESTED);
            commissionRepository.save(commission);
            return "redirect:/profile?success=commission_sent";
        }
        return "redirect:/";
    }

    @GetMapping("/artist/{artistId}")
    public String viewArtistCommissions(@PathVariable long artistId, Model model) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get());
            model.addAttribute("commissions", commissionRepository.findByArtist_Id(artistId));
            model.addAttribute("products", productRepository.findByArtist(artist.get()));
            return "artist-commissions";
        }
        return "redirect:/";
    }
}
