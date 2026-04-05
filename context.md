# KalaSetu Artisan Marketplace - Context & Changelog
**Status**: Finalized E-Commerce Backend & IP Protection Sprint (Java 17, Spring Boot, Native JPA)

This document is a direct historical snapshot of all the massive architectural and feature-level additions built to finalize the digital capabilities of the KalaSetu platform.

## 1. UX & Visual Architecture 
*   **The Artisan Heritage UI**: Entirely rewrote `style.css` driving an enterprise-grade dark/light mode palette heavily incorporating terracotta offsets, glassmorphism paneling on product cards, and embedded `Noto Serif` typography.
*   **Static Assets Localization**: Migrated all local imagery out of `/img/` directly into the Spring Boot root compile structure (`/src/main/resources/static/images/`) guaranteeing perfect visual loading inside the Thymeleaf UI templates.

## 2. Authentication & Provisioning Engine 
Overhauled the local Session system to create an entirely robust identity suite natively inside the MVC parameters.
*   **Unified Registration (`/register`)**: Added a highly secure Registration pipeline forcing users to explicitly label themselves natively as a `CUSTOMER` or an `ARTIST`. Solved foundational H2 Auto-Increment indexing overlaps securely using Sequence restarts.
*   **Auto-Provisioning**: When a user registers an `ARTIST` account, the backend instantaneously delegates and creates a brand new structural `Artist.java` portfolio linked specifically to their email address.
*   **Identity Mapping Fix**: Fixed native frontend navigational errors. Active HTTP Sessions actively map and store `artistId`, explicitly funneling dynamic dashboard button routes directly to `/commission/artist/{id}` based on the logged-in user.

## 3. Commercial Pipeline (Storefront & Payments)
*   **Refactored Endpoints**: Designed the `StorefrontController.java` dynamically separating the narrative landing page (`/`) safely away from the catalog grid (`/store`).
*   **The Checkout Gateway**: Scaffolded the `checkout.html` UI block. Piped the HTML form submission to a `@PostMapping` native catch. Handled safe mock transactions seamlessly, successfully redirecting successful clients back to the active storefront.
*   **Intelligent Popup Confirmations**: Created a dynamic Thymeleaf interceptor rendering a sliding green container block acknowledging **"Order Confirmed!"** strictly only when the explicit URL parameters resolve securely.

## 4. Trust & IP Protection (Watermarking & Hashing)
Replaced the community-moderation model with a robust server-side technical IP protection system that defends artisan assets at the image-serving layer.
*   **`ImageProtectionService.java`**: Computes a **64-bit perceptual hash (pHash)** fingerprint for each product image using a DCT algorithm (pure Java, no external libraries). Stores the hash on the `Product` entity (`imageHash` field) to enable future cross-catalog duplicate detection via Hamming distance comparison.
*   **Watermarking Engine**: At request time, `ImageProtectionService` burns a semi-transparent diagonal watermark ("© KalaSetu YYYY | Artist Name") plus a corner badge ("KalaSetu.in") directly into the image bytes using `Graphics2D`. The original static files are never modified.
*   **Protected Image Endpoint (`GET /images/protected/{productId}`)**: All Thymeleaf templates now reference this endpoint rather than raw static URLs. The `ImageController` streams the watermarked PNG back to the browser with `Cache-Control: no-store`, preventing caching of un-watermarked copies. Falls back gracefully to a redirect for any image format Java cannot decode natively.
