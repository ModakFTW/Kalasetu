package com.kalasetu.controller;

import com.kalasetu.model.Artist;
import com.kalasetu.model.Commission;
import com.kalasetu.model.CommissionStatus;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.CommissionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/commission")
public class CommissionController {

    private final CommissionRepository commissionRepository;
    private final ArtistRepository artistRepository;

    public CommissionController(CommissionRepository commissionRepository, ArtistRepository artistRepository) {
        this.commissionRepository = commissionRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/request/{artistId}")
    public String showRequestForm(@PathVariable long artistId, Model model) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get());
            model.addAttribute("commission", new Commission());
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
            return "redirect:/commission/artist/" + artistId + "?success";
        }
        return "redirect:/";
    }

    @GetMapping("/artist/{artistId}")
    public String viewArtistCommissions(@PathVariable long artistId, Model model) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get());
            model.addAttribute("commissions", commissionRepository.findByArtistId(artistId));
            return "artist-commissions";
        }
        return "redirect:/";
    }
}
