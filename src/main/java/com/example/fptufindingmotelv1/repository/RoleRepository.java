package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    @Query(value = "select new RoleModel(r.id, r.roleName, r.displayName) from RoleModel r")
    List<RoleModel> getAll();
}
