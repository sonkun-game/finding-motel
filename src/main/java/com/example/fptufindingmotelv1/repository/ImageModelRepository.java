package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel,Long> {

}
