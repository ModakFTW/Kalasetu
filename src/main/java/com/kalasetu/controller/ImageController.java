package com.kalasetu.controller;

import com.kalasetu.model.Product;
import com.kalasetu.repository.ProductRepository;
import com.kalasetu.service.ImageProtectionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.Optional;

/**
 * ImageController
 *
 * Serves all product images through a protected watermarked endpoint.
 * Direct access to /static/images/ is still possible for the browser,
 * but all Thymeleaf templates reference this endpoint instead.
 *
 * GET /images/protected/{productId}
 *   → Streams back a watermarked PNG of the product image.
 *   → Falls back to a redirect to the raw static URL if processing fails
 *     (e.g. AVIF or other unsupported format).
 */
@Controller
public class ImageController {

    private final ProductRepository productRepository;
    private final ImageProtectionService imageProtectionService;

    public ImageController(ProductRepository productRepository,
                           ImageProtectionService imageProtectionService) {
        this.productRepository = productRepository;
        this.imageProtectionService = imageProtectionService;
    }

    @GetMapping("/images/protected/{productId}")
    public ResponseEntity<byte[]> serveProtectedImage(@PathVariable Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOpt.get();
        String imageUrl = product.getImageUrl();

        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        String artistName = (product.getArtist() != null) ? product.getArtist().getName() : "KalaSetu Artist";

        // Attempt to generate watermarked PNG
        byte[] watermarkedBytes = imageProtectionService.generateWatermarkedImage(imageUrl, artistName);

        if (watermarkedBytes != null && watermarkedBytes.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            // Instruct browsers and CDNs not to cache — each request re-watermarks
            headers.setCacheControl("no-store");
            return new ResponseEntity<>(watermarkedBytes, headers, HttpStatus.OK);
        }

        // Fallback: redirect to the raw static asset URL
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(imageUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
