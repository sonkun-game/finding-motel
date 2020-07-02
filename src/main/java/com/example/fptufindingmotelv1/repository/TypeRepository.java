package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.TypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TypeModel, Long> {
}
