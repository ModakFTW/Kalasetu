package com.kalasetu.controller;

import com.kalasetu.model.User;
import com.kalasetu.model.UserRole;
import com.kalasetu.model.Artist;
import com.kalasetu.repository.UserRepository;
import com.kalasetu.repository.ArtistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    public AuthController(UserRepository userRepository, ArtistRepository artistRepository) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            session.setAttribute("user", user);

            if (user.getRole() == UserRole.ARTIST) {
                Optional<Artist> artistOpt = artistRepository.findAll().stream().filter(a -> a.getEmail().equals(email)).findFirst();
                if(artistOpt.isPresent()) {
                    session.setAttribute("artistId", artistOpt.get().getId());
                    return "redirect:/commission/artist/" + artistOpt.get().getId();
                }
                return "redirect:/artists"; 
            } else {
                return "redirect:/store";
            }
        }

        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam UserRole role, HttpSession session, Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        User newUser = new User(name, email, password, role);
        userRepository.save(newUser);
        session.setAttribute("user", newUser);

        if (role == UserRole.ARTIST) {
            Artist newArtist = new Artist(name, "A new artist to KalaSetu", email);
            artistRepository.save(newArtist);
            session.setAttribute("artistId", newArtist.getId());
            return "redirect:/commission/artist/" + newArtist.getId();
        }

        return "redirect:/store";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
