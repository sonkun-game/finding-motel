package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequestModel, String> {


    RentalRequestModel findByRentalRenterAndRentalRoom(RenterModel renterModel, RoomModel roomModel);

    @Query(value = "select rr from RentalRequestModel rr " +
            "where (:id is null or rr.id like :id )" +
            "and (:renterUsername is null or rr.rentalRenter.username like %:renterUsername%)" +
            "and (:roomId is null or rr.rentalRoom.id like :roomId)" +
            "and (:statusId is null or rr.rentalStatus.id = :statusId)")
    ArrayList<RentalRequestModel> searchRentalRequest(String id, String renterUsername, String roomId, Long statusId);
}
