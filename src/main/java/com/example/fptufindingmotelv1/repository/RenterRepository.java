package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RenterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RenterRepository extends JpaRepository<RenterModel, String> {
    RenterModel findByUsername(String username);

    @Query(value = "select new RenterModel(rt.username, r.id, r.roleName, r.displayName, " +
            "rt.fbAccount, rt.ggAccount, rt.phoneNumber, rt.displayName, rt.gender, rt.career, rt.dob) from RenterModel rt " +
            "join RoleModel r on r.id = rt.role.id " +
            "where rt.username = :username")
    RenterModel getRenterByUsername(String username);
}
