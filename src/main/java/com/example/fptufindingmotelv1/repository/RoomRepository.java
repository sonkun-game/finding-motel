package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomModel, String> {

    @Query(value = "select * from ROOM r " +
            "join RENTAL_REQUEST rq " +
            "on r.ID = rq.ROOM_ID " +
            "and (:statusId is null or rq.STATUS_ID = :statusId)" +
            "join POST p on p.ID = r.POST_ID " +
            "join LANDLORD llrd on llrd.USER_NAME = p.LANDLORD_ID " +
            "where (llrd.USER_NAME = :landlordId)" +
            "", nativeQuery = true)
    List<RoomModel> getListRoom(String landlordId, Long statusId);

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

}
