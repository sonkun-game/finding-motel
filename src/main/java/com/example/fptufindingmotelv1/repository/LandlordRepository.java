package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.LandlordModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandlordRepository extends JpaRepository<LandlordModel, String> {
}
