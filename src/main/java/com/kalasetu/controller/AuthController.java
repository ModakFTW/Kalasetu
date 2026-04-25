package com.kalasetu.controller;

import com.kalasetu.model.User;
import com.kalasetu.model.UserRole;
import com.kalasetu.model.Artist;
import com.kalasetu.repository.UserRepository;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.util.AadhaarValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

            if (user.getRole() == UserRole.ADMIN) {
                return "redirect:/admin/dashboard";
            } else if (user.getRole() == UserRole.ARTIST) {
                Optional<Artist> artistOpt = artistRepository.findByEmail(email);
                if (artistOpt.isPresent()) {
                    session.setAttribute("artistId", artistOpt.get().getId());
                    return "redirect:/commission/artist/" + artistOpt.get().getId();
                }
                return "redirect:/artists"; 
            } else {
                return "redirect:/store";
            }
        }

        model.addAttribute("error", "error.invalid_credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String name, 
                                @RequestParam String email, 
                                @RequestParam String password, 
                                @RequestParam UserRole role,
                                @RequestParam String phoneNumber,
                                @RequestParam(required = false) String craftType,
                                @RequestParam(required = false) String aadhaarNumber,
                                @RequestParam(required = false) MultipartFile profilePhoto,
                                HttpSession session, Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "error.email_registered");
            return "register";
        }

        if (role == UserRole.ARTIST) {
            if (aadhaarNumber == null || !AadhaarValidator.validateAadhaar(aadhaarNumber)) {
                model.addAttribute("error", "error.invalid_aadhaar");
                return "register";
            }
        }

        User newUser = new User(name, email, password, role);
        newUser.setPhoneNumber(phoneNumber);
        userRepository.save(newUser);
        session.setAttribute("user", newUser);

        if (role == UserRole.ARTIST) {
            Artist newArtist = new Artist(name, "A skilled artisan specializing in " + (craftType != null ? craftType : "traditional crafts"), email);
            newArtist.setPhoneNumber(phoneNumber);
            newArtist.setCraftType(craftType);
            newArtist.setAadhaarNumber(aadhaarNumber);
            
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID().toString() + "_" + profilePhoto.getOriginalFilename();
                    Path uploadPath = Paths.get("src/main/resources/static/images/artists/");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(profilePhoto.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    newArtist.setProfilePictureUrl("/images/artists/" + fileName);
                } catch (IOException e) {
                    newArtist.setProfilePictureUrl("/images/placeholder-artist.jpg");
                }
            } else {
                newArtist.setProfilePictureUrl("/images/placeholder-artist.jpg");
            }
            
            artistRepository.save(newArtist);
            session.setAttribute("artistId", newArtist.getId());
            return "redirect:/commission/artist/" + newArtist.getId() + "?registrationPending";
        }

        return "redirect:/store";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
