package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.NotificationModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationModel, String> {
    @Query(value = "select count(ntf) from NotificationModel ntf " +
            "where (ntf.userNotification.username = :username)" +
            "and (ntf.statusNotification.id = :statusId)")
    int getNotificationNumber(String username, Long statusId);

    @Query(value = "select ntf from NotificationModel ntf " +
            "where (ntf.userNotification.username = :username)" +
            "order by ntf.createdDate desc ")
    List<NotificationModel> getAllByUsername(String username);

    @Query(value = "select new NotificationModel(ntf.id, ntf.content, ntf.createdDate, " +
            "ntf.statusNotification.id, rq.id, rq.rentalRoom.id) from NotificationModel ntf " +
            "join RentalRequestModel rq on ntf.rentalRequestNotification.id = rq.id " +
            "where (ntf.userNotification.username = :username)" +
            "order by ntf.createdDate desc ")
    Slice<NotificationModel> getAllByUsernamePaging(String username, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete n from NOTIFICATION n " +
            "where n.CREATED_DATE < :createDateExpire " +
            "and n.STATUS_ID = :statusId " +
            "and n.USER_ID = :username", nativeQuery = true)
    void removeNotifications(Date createDateExpire, Long statusId, String username);

    @Transactional
    @Modifying
    @Query(value = "delete n from NOTIFICATION n " +
            "inner join RENTAL_REQUEST rq on rq.ID = n.REQUEST_ID " +
            "inner join ROOM r on rq.ROOM_ID = r.ID " +
            "where r.POST_ID = :postId ", nativeQuery = true)
    void deleteNotificationsByPost(String postId);
}
