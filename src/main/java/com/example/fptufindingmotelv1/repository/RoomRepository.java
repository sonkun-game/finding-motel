package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomModel, String> {

    @Query(value = "select r from RoomModel r " +
            "where (:landlordId is null or r.postRoom.landlord.username = :landlordId)" +
            "and (:postId is null or r.postRoom.id = :postId)" +
            "and (:roomId is null or r.id = :roomId)" +
            " order by r.name asc ")
    List<RoomModel> getListRoom(String landlordId, String postId, String roomId);

    @Query(value = "select new RoomModel(r.id, r.name, s.id) from RoomModel r " +
            "join StatusModel s on r.status.id = s.id " +
            "where (:postId is null or r.postRoom.id = :postId)" +
            "order by r.name asc ")
    List<RoomModel> getListRoomByPostId(String postId);

    @Transactional
    @Modifying
    @Query(value = "delete r from ROOM r " +
            "where r.POST_ID = :postId ", nativeQuery = true)
    void deleteRoomsByPost(String postId);

    @Query(value = "select new RoomModel(r.id, r.name, p.id, p.title, s.id, s.status, count(rq.id)) from RoomModel r " +
            "join PostModel p on r.postRoom.id = p.id " +
            "join StatusModel s on r.status.id = s.id " +
            "left outer join RentalRequestModel rq on rq.rentalRoom.id = r.id " +
            "where (:roomId is null or r.id = :roomId)" +
            "and (:postId is null or p.id = :postId) " +
            "and (:statusId is null or rq.rentalStatus.id = :statusId)" +
            "and (:landlordUsername is null or p.landlord.username = :landlordUsername)" +
            "group by r.id, r.name, p.id, p.title, s.id, s.status " +
            "order by r.name asc ")
    Page<RoomModel> getRooms(String roomId, String postId, Long statusId, String landlordUsername, Pageable pageable);

    @Query(value = "select new RoomModel(r.id, r.name, p.id, p.title, s.id, s.status, count(rq.id)) from RoomModel r " +
            "join PostModel p on r.postRoom.id = p.id " +
            "join StatusModel s on r.status.id = s.id " +
            "left outer join RentalRequestModel rq on rq.rentalRoom.id = r.id " +
            "where (:roomId is null or r.id = :roomId)" +
            "and (:postId is null or p.id = :postId) " +
            "and (:statusId is null or rq.rentalStatus.id = :statusId)" +
            "group by r.id, r.name, p.id, p.title, s.id, s.status " +
            "order by r.name asc ")
    List<RoomModel> getRooms(String roomId, String postId, Long statusId);

    @Query(value = "select new RoomModel(r.id, r.name, p.id, p.title) from RoomModel r " +
            "join PostModel p on r.postRoom.id = p.id " +
            "where (:roomId is null or r.id = :roomId)")
    RoomModel getRoomById(String roomId);

    @Transactional
    @Modifying
    @Query(value = "update RoomModel r " +
            "set r.status.id = :statusId " +
            "where r.id = :roomId")
    void updateStatusRoom(String roomId, Long statusId);

}
