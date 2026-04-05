package com.kalasetu.controller;

import com.kalasetu.model.Artist;
import com.kalasetu.model.Product;
import com.kalasetu.repository.ArtistRepository;
import com.kalasetu.repository.ProductRepository;
import com.kalasetu.service.ImageProtectionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final ProductRepository productRepository;
    private final ImageProtectionService imageProtectionService;

    // Define the local upload directory
    private final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";

    public ArtistController(ArtistRepository artistRepository, 
                            ProductRepository productRepository,
                            ImageProtectionService imageProtectionService) {
        this.artistRepository = artistRepository;
        this.productRepository = productRepository;
        this.imageProtectionService = imageProtectionService;
    }

    @GetMapping("/artist/profile/{id}")
    public String artistProfile(@PathVariable Long id, Model model) {
        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            model.addAttribute("artist", artist);
            List<Product> products = productRepository.findByArtist(artist);
            model.addAttribute("products", products);
            return "artist-profile";
        }
        return "redirect:/artists";
    }

    @PostMapping("/artist/{id}/product/upload")
    public String uploadProduct(@PathVariable Long id,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") Double price,
                                @RequestParam("image") MultipartFile image,
                                HttpSession session) {
        
        // Ensure only the logged-in artist can upload to their own catalog
        Long sessionArtistId = (Long) session.getAttribute("artistId");
        if (sessionArtistId == null || !sessionArtistId.equals(id)) {
            return "redirect:/login";
        }

        Optional<Artist> artistOpt = artistRepository.findById(id);
        if (artistOpt.isEmpty()) {
            return "redirect:/artists";
        }

        Artist artist = artistOpt.get();

        String imageUrl = "";
        
        if (!image.isEmpty()) {
            try {
                // Ensure upload directory exists
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate unique filename
                String originalFilename = image.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;
                
                Path filePath = uploadPath.resolve(newFilename);
                Files.write(filePath, image.getBytes());
                
                // The URL mapping based on static assets
                imageUrl = "/images/uploads/" + newFilename;
                
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error appropriately, potentially redirect with an error message
                return "redirect:/commission/artist/" + id + "?error=upload_failed";
            }
        }

        // Create the product
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setImageUrl(imageUrl);
        newProduct.setArtist(artist);
        
        // We calculate the hash using the protection service
        if (!imageUrl.isEmpty()) {
             // For hashing, imageProtectionService might expect absolute path or URI if it reads local
             // Actually, let's look at ImageProtectionService and see if we can trigger hash generation
             try {
                 String hash = imageProtectionService.computeHash(imageUrl);
                 newProduct.setImageHash(hash);
             } catch (Exception e) {
                 e.printStackTrace();
             }
        }

        productRepository.save(newProduct);

        return "redirect:/commission/artist/" + id + "?success=product_uploaded";
    }
}
