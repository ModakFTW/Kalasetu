package com.kalasetu.repository;

import com.kalasetu.model.Artist;
import com.kalasetu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByArtist(Artist artist);
    List<Product> findByCategoryId(Long categoryId);
}
