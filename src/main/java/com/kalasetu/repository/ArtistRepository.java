package com.kalasetu.repository;

import com.kalasetu.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByEmail(String email);
    List<Artist> findByApprovedTrue();
    List<Artist> findByApprovedFalse();
}
