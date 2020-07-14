package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequestModel, String> {

    RentalRequestModel findByRentalRenterAndRentalRoom(RenterModel renterModel, RoomModel roomModel);
    List<RentalRequestModel> findAllByRentalRoom(RoomModel roomModel);
}
