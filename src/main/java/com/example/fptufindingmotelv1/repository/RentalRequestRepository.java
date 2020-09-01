package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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


    @Query(value = "select new RentalRequestModel(rr.id, rr.requestDate, rr.startDate, " +
            "rr.cancelDate, rr.expireMessage, p.id, p.title, p.landlord.username, r.id, r.name, s.id, s.status) from RentalRequestModel rr " +
            "join RoomModel r on rr.rentalRoom.id = r.id " +
            "join PostModel p on r.postRoom.id = p.id " +
            "join StatusModel s on rr.rentalStatus.id = s.id " +
            "where (:id is null or rr.id = :id )" +
            "and (:renterUsername is null or rr.rentalRenter.username = :renterUsername)" +
            "and (:roomId is null or rr.rentalRoom.id like :roomId)" +
            "and (:statusId is null or rr.rentalStatus.id = :statusId)" +
            "")
    Page<RentalRequestModel> searchRentalRequest(String id, String renterUsername, String roomId, Long statusId, Pageable pageable);

    @Query(value = "select count(rr.id) from RentalRequestModel rr " +
            "where (rr.rentalRoom.postRoom.landlord.username = :landlordUsername)" +
            "and rr.rentalStatus.id = :statusId")
    int getRequestNumber(String landlordUsername, Long statusId);

    @Transactional
    @Modifying
    @Query(value = "delete rq from RENTAL_REQUEST rq " +
            "where rq.CANCEL_DATE < :cancelDateExpire " +
            "and rq.STATUS_ID = :statusId " +
            "and rq.RENTER_ID = :renterUsername", nativeQuery = true)
    void removeRentalRequest(Date cancelDateExpire, Long statusId, String renterUsername);

    @Transactional
    @Modifying
    @Query(value = "delete rq from RENTAL_REQUEST rq " +
            "inner join ROOM r on rq.ROOM_ID = r.ID " +
            "where r.POST_ID = :postId ", nativeQuery = true)
    void deleteRentalRequestsByPost(String postId);

    @Query(value = "select new RentalRequestModel(rq.id, rq.requestDate, rq.startDate, rq.cancelDate, " +
            "rq.expireMessage, r.username, s.id, s.status) from RentalRequestModel rq " +
            "join RenterModel r on rq.rentalRenter.username = r.username " +
            "join StatusModel s on rq.rentalStatus.id = s.id " +
            "where (:requestId is null or rq.id = :requestId)" +
            "and (:roomId is null or rq.rentalRoom.id = :roomId) " +
            "and (:statusId is null or rq.rentalStatus.id = :statusId)" +
            "order by rq.requestDate desc ")
    List<RentalRequestModel> getRentalRequests(String requestId, String roomId, Long statusId);

    Boolean existsByRentalRenterAndRentalRoomAndRentalStatus(RenterModel renter, RoomModel room, StatusModel status);

    Boolean existsByRentalRenterAndRentalStatus(RenterModel renter, StatusModel status);

    @Transactional
    @Modifying
    @Query(value = "update RentalRequestModel r " +
            "set r.cancelDate = :cancelDate, " +
            "r.rentalStatus.id = :statusId " +
            "where r.id = :requestId")
    void updateCancelStatus(String requestId, Date cancelDate, Long statusId);

    @Transactional
    @Modifying
    @Query(value = "update RentalRequestModel r " +
            "set r.expireMessage = :expireMessage, " +
            "r.rentalStatus.id = :statusId " +
            "where r.id = :requestId")
    void updateExpireStatus(String requestId, String expireMessage, Long statusId);

    @Modifying
    @Transactional
    @Query(value = "insert into RENTAL_REQUEST (ID, RENTER_ID, ROOM_ID, REQUEST_DATE, " +
            "START_DATE, STATUS_ID) " +
            "values (:id, :renterUsername, :roomId, :requestDate, :startDate, :statusId)", nativeQuery = true)
    void insertRentalRequest(String id, String renterUsername, String roomId, Date requestDate,
                             Date startDate, Long statusId);
}
