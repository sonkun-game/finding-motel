package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.FilterPostModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterPostRepository extends JpaRepository<FilterPostModel, Long> {
    List<FilterPostModel> findAllByType(String type);
}
