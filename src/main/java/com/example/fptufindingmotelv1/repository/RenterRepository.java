package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RenterModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenterRepository extends JpaRepository<RenterModel, String> {
    RenterModel findByUsername(String username);
}
