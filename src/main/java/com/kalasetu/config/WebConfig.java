package com.kalasetu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose the artist images directory to be served at /images/artists/**
        Path artistUploadDir = Paths.get("src/main/resources/static/images/artists/");
        String artistUploadPath = artistUploadDir.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/images/artists/**")
                .addResourceLocations("file:" + artistUploadPath + "/");

        // Also expose general images if needed
        Path generalUploadDir = Paths.get("src/main/resources/static/images/");
        String generalUploadPath = generalUploadDir.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + generalUploadPath + "/", "classpath:/static/images/");
    }
}
