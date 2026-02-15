# Kalasetu - Project Report

This document serves as detailed documentation of the Kalasetu E-commerce Application project, outlining the features implemented, the technology stack, major code components, and the future roadmap. This is intended to be used as a reference for project evaluation.

## 1. Project Overview
**Kalasetu** ("Bridge of Art") is a monolithic web application designed to connect local artisans with buyers. It focuses on authenticity, community, and direct support for creators.

### Core Technologies
-   **Framework**: Spring Boot 3 (Java 17)
-   **Database**: H2 In-Memory Database (for development/demo)
-   **Frontend**: Thymeleaf (Server-side rendering) + Vanilla CSS (Rustic Minimalist Theme)
-   **Build Tool**: Maven

---

## 2. Implemented Features

### 2.1. Domain Scaffolding
We established the core data models to represent the localized marketplace:
-   `Product`: Represents items for sale. Includes flexible attributes (materials, dimensions, etc.) stored as a `Map`.
-   `Artist`: Represents the creator (name, bio, location).
-   `Category`: For organizing products (Pottery, Textiles, etc.).
-   `Commission`: For handling custom requests from buyers.

### 2.2. Frontend Design System
A custom "Rustic Minimalist" design system was implemented from scratch:
-   **Palette**: Cream (`#F2EEE3`), Sage (`#BAAF92`), Walnut Brown (`#785E4D`), Terracotta (`#FF8426`).
-   **Typography**: *Playfair Display* (Headings) and *Inter* (Body).
-   **Layout**: A reusable Thymeleaf Layout (`layout.html`) ensures consistent Header/Footer across all pages.
-   **Landing Page**: A narrative-driven home page highlighting mission and artisan stories.
-   **Store Page**: A dedicated product catalog grid.

### 2.3. Custom Commissions Workflow
A key differentiator feature allowing buyers to request bespoke items:
1.  User clicks "Request Custom" on a product or artist profile.
2.  Fills out a form with budget, description, and contact info.
3.  Request is saved with status `REQUESTED`.
4.  Artists can view requests via their dashboard.

---

## 3. Key Code Explanations

### 3.1. Data Modeling (JPA Entities)
Located in `src/main/java/com/kalasetu/model/`

**Code Highlight: `Product.java`**
```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Relationship: Many Products belong to One Artist
    @ManyToOne 
    @JoinColumn(name = "artist_id")
    private Artist artist;

    // Flexible storage for product specs (e.g. Dimensions: 10x10, Material: Clay)
    // Avoids creating complex separate tables for simple attributes.
    @ElementCollection 
    private Map<String, String> attributes = new HashMap<>();
}
```
*Why this matters:* The `@ElementCollection` allows us to store varying product details (which differ between pottery and textiles) without a rigid schema.

**Code Highlight: `Commission.java`**
```java
@Entity
public class Commission {
    // Relationship: Who is this request for?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Enumerated(EnumType.STRING)
    private CommissionStatus status = CommissionStatus.REQUESTED; // State Machine: REQUESTED -> ACCEPTED -> COMPLETED
}
```

### 3.2. Controller Logic (Spring MVC)
Located in `src/main/java/com/kalasetu/controller/`

**Code Highlight: `StorefrontController.java`**
```java
@Controller
public class StorefrontController {
    
    // Auto-wires the Repository to access database
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/store") // Maps HTTP GET /store to this method
    public String store(Model model) {
        // Fetches all products and puts them into the 'model' container
        model.addAttribute("products", productRepository.findAll());
        
        // Tells Spring to render 'store.html' template
        return "store";
    }
}
```

**Code Highlight: `CommissionController.java`**
```java
@PostMapping("/request") // Handles Form Submission
public String submitRequest(@RequestParam long artistId, @ModelAttribute Commission commission) {
    Optional<Artist> artist = artistRepository.findById(artistId);
    if (artist.isPresent()) {
        commission.setArtist(artist.get());
        commissionRepository.save(commission); // Saves to H2 Database
        return "redirect:/commission/artist/" + artistId + "?success"; // PRG Pattern (Post-Redirect-Get)
    }
    return "redirect:/";
}
```
*Why this matters:* The generic `submitRequest` handles the business logic of linking a request to an artist and persisting it, then safely redirecting the user to prevent duplicate submissions on refresh.

### 3.3. Frontend Templating (Thymeleaf)
Located in `src/main/resources/templates/`

**Code Highlight: `layout.html` (The "Master" Template)**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" 
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"> <!-- Layout Dialect Namespace -->
<body>
    <header>...</header> <!-- Common Header -->
    
    <div layout:fragment="content">
        <!-- Pages like home.html inject their specific content here -->
    </div>

    <footer>...</footer> <!-- Common Footer -->
</body>
</html>
```

**Code Highlight: `store.html` (Iterating Data)**
```html
<div th:each="product : ${products}" class="card"> <!-- Loops through list of products -->
    <h3 th:text="${product.name}">Product Name</h3> <!-- Injects dynamic name -->
    <p th:text="${'$' + product.price}">$0.00</p>
    <a th:href="@{/products/{id}(id=${product.id})}">View Details</a> <!-- Generates dynamic URL -->
</div>
```

---

## 4. Future Roadmap (What's Next)

### 4.1. User Authentication & Authorization (Next Step)
**Goal:** Secure the application so only registered Users can request commissions and only Artists can view their dashboards.
*   **Plan:** Implement Spring Security.
*   **Entities:** Create `User` (Role: BUYER, ARTIST, ADMIN) and link it to `Artist` profile.

### 4.2. Shopping Cart & Checkout
**Goal:** Allow users to buy multiple items.
*   **Plan:** Use `@SessionScope` bean to store a `Cart` object across requests. Implement a simple Checkout flow.

### 4.3. Production Database
**Goal:** Persist data permanently.
*   **Plan:** Migrate from H2 (In-Memory) to PostgreSQL or MySQL.

### 4.4. Deployment
**Goal:** Make the site live.
*   **Plan:** Dockerize the application and deploy to a cloud provider (e.g., Render, Railway, or AWS).
