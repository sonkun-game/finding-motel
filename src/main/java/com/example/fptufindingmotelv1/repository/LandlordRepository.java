package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.LandlordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LandlordRepository extends JpaRepository<LandlordModel, String> {
    LandlordModel findByUsername(String username);

    @Query(value = "select new LandlordModel(ll.username, ll.amount, ll.unBanDate) from LandlordModel ll " +
            "where (:landlordUsername is null or ll.username = :landlordUsername)" +
            "")
    LandlordModel getLandlordById(String landlordUsername);

    @Transactional
    @Modifying
    @Query(value = "update LandlordModel ll " +
            "set ll.amount = :amount " +
            "where ll.username = :landlordUsername ")
    void updateAmountLandlord(Float amount, String landlordUsername);
}
