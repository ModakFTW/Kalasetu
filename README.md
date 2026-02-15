# Kalasetu - Local Art Store

A Spring Boot monolithic e-commerce application for local artists.

## Prerequisites

-   Java 17 or higher
-   Maven 3.8+ (ensure `mvn` is in your PATH)

## Setup

1.  **Clone/Download** the project.
2.  **Navigate** to the project directory:
    ```bash
    cd kalasetu
    ```

## Running the Application

### Option 1: Using the Helper Script (Recommended)
If you don't have Java 17 or Maven installed globally, run the provided script:
```powershell
.\run_dev.ps1
```

### Option 2: Using Global Maven
If you have `mvn` and Java 17+ installed:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Features

-   **Storefront**: Browse products by local artists.
-   **Product Details**: View flexible product attributes (dimensions, materials, etc.).
-   **H2 Database**: In-memory database for rapid development.
    -   Console: `http://localhost:8080/h2-console`
    -   JDBC URL: `jdbc:h2:mem:testdb`
    -   User: `sa`
    -   Password: `password`

## Project Structure

-   `src/main/java/com/kalasetu/model`: Entity classes (Artist, Category, Product).
-   `src/main/java/com/kalasetu/controller`: Web controllers.
-   `src/main/resources/templates`: Thymeleaf HTML templates.
-   `src/main/resources/data.sql`: Initial seed data.
"# Kalasetu" 
