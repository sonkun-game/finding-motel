package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequestModel, String> {

}
