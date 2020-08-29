package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.NotificationModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(value = "select ntf from NotificationModel ntf " +
            "where (ntf.userNotification.username = :username)" +
            "order by ntf.createdDate desc ")
    Slice<NotificationModel> getAllByUsernamePaging(String username, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete from NotificationModel n " +
            "where n.rentalRequestNotification.rentalRoom.postRoom.id = :postId " +
            "")
    void removeNotificationByPost(String postId);
}
