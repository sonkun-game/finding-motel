package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.WishListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishListModel, String> {

    @Transactional
    @Modifying
    @Query(value = "delete wl from WISH_LIST wl " +
            "where wl.POST_ID = :postId ", nativeQuery = true)
    void deleteWishListsByPost(String postId);

    @Transactional
    @Modifying
    @Query(value = "delete wl from WISH_LIST wl " +
            "where wl.POST_ID = :postId " +
            "and wl.RENTER_ID = :renterUsername", nativeQuery = true)
    void deleteWishListByPostAndRenter(String postId, String renterUsername);

    @Transactional
    @Modifying
    @Query(value = "delete wl from WISH_LIST wl " +
            "where wl.ID = :id ", nativeQuery = true)
    void deleteWishListById(String id);

    @Query(value = "select new WishListModel(wl.id, wl.createdDate, wl.wishListPost.id, wl.wishListPost.price, " +
            "wl.wishListPost.distance, wl.wishListPost.square, " +
            "wl.wishListPost.description, wl.wishListPost.title, " +
            "wl.wishListPost.address, MAX (im.id)) from WishListModel wl " +
            "join ImageModel im on wl.wishListPost.id = im.post.id " +
            "where (:renterId is null or wl.wishListRenter.username = :renterId)" +
            "and (:isVisible is null or wl.wishListPost.visible = :isVisible)" +
            "and (:banned is null or wl.wishListPost.banned = :banned)" +
            "and (:currentDate is null or wl.wishListPost.expireDate >= :currentDate)" +
            "group by wl.id, wl.createdDate, wl.wishListPost.id, wl.wishListPost.price, wl.wishListPost.distance, " +
            "wl.wishListPost.square, wl.wishListPost.description, wl.wishListPost.title, wl.wishListPost.address " +
            "order by wl.createdDate desc " +
            "")
    List<WishListModel> getWishlistOfRenter(String renterId, Boolean isVisible, Boolean banned, Date currentDate);
}
