package com.kalasetu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nameHi;
    private String nameMr;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
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

    public String getLocalizedName() {
        String lang = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage();
        if ("hi".equals(lang) && nameHi != null && !nameHi.isEmpty()) return nameHi;
        if ("mr".equals(lang) && nameMr != null && !nameMr.isEmpty()) return nameMr;
        return name;
    }
}
