package com.kalasetu.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nameHi;
    private String nameMr;
    
    private String description;
    private String descriptionHi;
    private String descriptionMr;

    private Double price;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ElementCollection
    @CollectionTable(name = "product_attributes", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes = new HashMap<>();

    /**
     * Perceptual hash (pHash) of the product image.
     * Computed by ImageProtectionService at upload time.
     * Used to detect visually duplicate images across the catalog.
     */
    private String imageHash;

    /**
     * Quantity of this product currently in stock.
     */
    private Integer inventoryCount;

    /**
     * Whether the artist has marked this product as available.
     */
    private Boolean isAvailable = true;

    public Product() {

    }

    public Product(String name, String description, Double price, String imageUrl, Artist artist, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.artist = artist;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHi() {
        return nameHi;
    }

    public void setNameHi(String nameHi) {
        this.nameHi = nameHi;
    }

    public String getNameMr() {
        return nameMr;
    }

    public void setNameMr(String nameMr) {
        this.nameMr = nameMr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionHi() {
        return descriptionHi;
    }

    public void setDescriptionHi(String descriptionHi) {
        this.descriptionHi = descriptionHi;
    }

    public String getDescriptionMr() {
        return descriptionMr;
    }

    public void setDescriptionMr(String descriptionMr) {
        this.descriptionMr = descriptionMr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getImageHash() { return imageHash; }
    public void setImageHash(String imageHash) { this.imageHash = imageHash; }

    public Integer getInventoryCount() { return inventoryCount; }
    public void setInventoryCount(Integer inventoryCount) { this.inventoryCount = inventoryCount; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getLocalizedName() {
        String lang = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage();
        if ("hi".equals(lang) && nameHi != null && !nameHi.isEmpty()) return nameHi;
        if ("mr".equals(lang) && nameMr != null && !nameMr.isEmpty()) return nameMr;
        return name;
    }

    public String getLocalizedDescription() {
        String lang = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage();
        if ("hi".equals(lang) && descriptionHi != null && !descriptionHi.isEmpty()) return descriptionHi;
        if ("mr".equals(lang) && descriptionMr != null && !descriptionMr.isEmpty()) return descriptionMr;
        return description;
    }
}
