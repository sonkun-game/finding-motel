package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<StatusModel, Long> {
    StatusModel findByIdAndType(long id, long type);
    List<StatusModel> findAllByType(long type);
}
