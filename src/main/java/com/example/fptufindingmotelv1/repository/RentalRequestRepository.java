package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequestModel, String> {

    List<RentalRequestModel> findByRentalRenterAndRentalRoom(RenterModel renterModel, RoomModel roomModel);
    @Query(value = "select r from RentalRequestModel r " +
            "where 1 = 1 " +
            "and (:landlordId is null or r.rentalRoom.postRoom.landlord.username = :landlordId)" +
            "and (:statusId is null or r.rentalStatus.id = :statusId)" +
            "and (:renterId is null or r.rentalRenter.username = :renterId)" +
            "and (:roomId is null or r.rentalRoom.id = :roomId)" +
            "")
    List<RentalRequestModel> getListRequest(String landlordId, Long statusId, String renterId, String roomId);


    @Query(value = "select rr from RentalRequestModel rr " +
            "where (:id is null or rr.id like :id )" +
            "and (:renterUsername is null or rr.rentalRenter.username = :renterUsername)" +
            "and (:roomId is null or rr.rentalRoom.id like :roomId)" +
            "and (:statusId is null or rr.rentalStatus.id = :statusId)" +
            "and (:requestId is null or rr.id = :requestId)" +
            "order by rr.requestDate desc")
    ArrayList<RentalRequestModel> searchRentalRequest(String id, String renterUsername, String roomId, Long statusId, String requestId);

    @Query(value = "select count(rr) from RentalRequestModel rr " +
            "where (rr.rentalRoom.postRoom.landlord.username = :landlordUsername)" +
            "and rr.rentalStatus.id = :statusId")
    int getRequestNumber(String landlordUsername, Long statusId);

    @Transactional
    @Modifying
    @Query(value = "delete from RentalRequestModel r " +
                "where r.cancelDate < :cancelDateExpire " +
                "and r.rentalStatus.id = :statusId " +
                "and r.rentalRenter.username = :renterUsername")
    void removeRentalRequest(Date cancelDateExpire, Long statusId, String renterUsername);
}
