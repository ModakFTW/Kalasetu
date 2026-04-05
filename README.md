# KalaSetu — The Digital Curator

> A full-stack artisan marketplace connecting local craftspeople with collectors worldwide.

Built with **Java 17 · Spring Boot 3 · Thymeleaf · H2 Database · Vanilla CSS**

---

## Overview

KalaSetu is a monolithic e-commerce platform designed to authentically represent and sell the work of local artisans. It features a complete user identity system with two distinct account types, a commission request workflow, an IP theft reporting engine, and a curated product storefront.

---

## Features

### 🛍️ Storefront & Commerce
- Browse the curated product catalog at `/store`
- Product detail pages with artisan attribution, specs, and attributes
- Full checkout flow with mock payment processing
- "Order Confirmed" notification banner on successful purchase

### 👤 Authentication & Identity
- **Customer accounts** — browse and purchase artisan products
- **Artist accounts** — manage a personal portfolio and receive commission requests
- Session-based login / logout tied to user role
- `/register` — Role-selection form (Customer or Artist) with auto-provisioning:
  - Registering as an Artist instantly creates a linked `Artist` portfolio entity

### 🎨 Artist Dashboard & Commissions
- Artists log in and are directed straight to their personal commission dashboard
- Collectors can browse the Artist directory at `/artists` and submit commission requests
- Dashboard displays incoming requests with status (REQUESTED / ACCEPTED / DECLINED / COMPLETED)

### 🛡️ IP Theft & Moderation
- Every product detail page includes a **"Report IP Theft / Duplicate"** form
- Reports are logged to a backend `ItemReport` table with timestamps and reasons
- Products are **automatically hidden from the storefront** once they accumulate **10 or more community reports**
- Flagged products are removed from the public grid without deleting underlying data

---

## Demo Accounts

| Role     | Email                  | Password  |
|----------|------------------------|-----------|
| Customer | `jane@collector.com`   | `password` |
| Artist   | `anya@artisan.com`     | `password` |
| Artist   | `benji@artisan.com`    | `password` |

---

## Running the Application

### Recommended (no global Java/Maven needed)
```powershell
.\run_dev.ps1
```

### Alternative (if `mvn` and Java 17 are globally installed)
```bash
mvn spring-boot:run
```

App starts at **http://localhost:8080**

---

## H2 Database Console

| Property | Value                     |
|----------|---------------------------|
| URL      | `http://localhost:8080/h2-console` |
| JDBC URL | `jdbc:h2:mem:testdb`      |
| User     | `sa`                      |
| Password | `password`                |

---

## Project Structure

```
src/main/java/com/kalasetu/
├── model/
│   ├── Artist.java           # Artisan entity (name, bio, email, catalog)
│   ├── Category.java         # Product categorisation
│   ├── Commission.java       # Commission request entity
│   ├── ItemReport.java       # IP theft report log
│   ├── Product.java          # Product entity (with hidden flag)
│   ├── User.java             # Platform user (email, password, role)
│   └── UserRole.java         # Enum: CUSTOMER | ARTIST
│
├── repository/
│   ├── ArtistRepository.java
│   ├── CategoryRepository.java
│   ├── CommissionRepository.java
│   ├── ItemReportRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
│
└── controller/
    ├── AuthController.java         # /login, /register, /logout
    ├── CommissionController.java   # /commission/** (submit & dashboard)
    ├── ReportController.java       # /report/product (IP flagging)
    └── StorefrontController.java   # /, /store, /product/{id}, /checkout, /artists

src/main/resources/
├── templates/
│   ├── layout.html             # Global header/footer/nav shell
│   ├── home.html               # Landing page
│   ├── store.html              # Product grid
│   ├── product-detail.html     # Individual product + report form
│   ├── checkout.html           # Checkout form
│   ├── login.html              # Login page
│   ├── register.html           # Registration with role selector
│   ├── artists.html            # Artisan directory
│   ├── commission-form.html    # Commission request form
│   └── artist-commissions.html # Artist dashboard
├── static/
│   ├── css/style.css           # Artisan Heritage design system
│   └── images/                 # Locally hosted product & logo images
├── data.sql                    # Seed data (users, artists, products)
└── application.properties
```

---

## Legal

All artwork images and product designs displayed on KalaSetu are the exclusive copyrighted property of their respective artisans. Unauthorised reproduction or redistribution is strictly prohibited.

© 2026 KalaSetu Platform & Marketplace. All rights reserved.
