package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.TypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<TypeModel, Long> {

    @Query(value = "select new TypeModel(t.id, t.name) from TypeModel t")
    List<TypeModel> getAllTypeOfPost();
}
