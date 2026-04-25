package com.kalasetu.repository;

import com.kalasetu.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByArtist_Id(Long artistId);
    List<Commission> findByRequesterEmail(String email);
}
