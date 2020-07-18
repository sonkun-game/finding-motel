package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;


@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequestModel, String> {

    RentalRequestModel findByRentalRenterAndRentalRoom(RenterModel renterModel, RoomModel roomModel);
    @Query(value = "select r from RentalRequestModel r " +
            "where 1 = 1 " +
            "and (:landlordId is null or r.rentalRoom.postRoom.landlord.username = :landlordId)" +
            "and (:statusId is null or r.rentalStatus.id = :statusId)" +
            "and (:renterId is null or r.rentalRenter.username = :renterId)" +
            "")
    List<RentalRequestModel> getListRequest(String landlordId, Long statusId, String renterId);


    @Query(value = "select rr from RentalRequestModel rr " +
            "where (:id is null or rr.id like :id )" +
            "and (:renterUsername is null or rr.rentalRenter.username = :renterUsername)" +
            "and (:roomId is null or rr.rentalRoom.id like :roomId)" +
            "and (:statusId is null or rr.rentalStatus.id = :statusId)" +
            "order by rr.requestDate desc")
    ArrayList<RentalRequestModel> searchRentalRequest(String id, String renterUsername, String roomId, Long statusId);

    @Query(value = "select count(rr) from RentalRequestModel rr " +
            "where (rr.rentalRoom.postRoom.landlord.username = :landlordUsername)" +
            "and rr.rentalStatus.id = :statusId")
    int getRequestNumber(String landlordUsername, Long statusId);

    List<RentalRequestModel> findAllByRentalRoom(RoomModel room);

}
