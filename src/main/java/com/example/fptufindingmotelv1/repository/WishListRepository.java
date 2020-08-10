package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishListModel, String> {
    List<WishListModel> findAllByWishListRenter(RenterModel renter, Sort sort);

    WishListModel findByWishListPostAndWishListRenter(PostModel post, RenterModel renter);

    @Query(value = "select new PostModel(wl.wishListPost.id, wl.wishListPost.price, " +
            "wl.wishListPost.distance, wl.wishListPost.square, " +
            "wl.wishListPost.description, wl.wishListPost.title, " +
            "wl.wishListPost.address, MAX (im.id)) from WishListModel wl " +
            "join ImageModel im on wl.wishListPost.id = im.post.id " +
            "where (:renterId is null or wl.wishListRenter.username = :renterId)" +
            "and (:isVisible is null or wl.wishListPost.visible = :isVisible)" +
            "and (:banned is null or wl.wishListPost.banned = :banned)" +
            "group by wl.wishListPost.id, wl.wishListPost.price, wl.wishListPost.distance, " +
            "wl.wishListPost.square, wl.wishListPost.description, wl.wishListPost.title, wl.wishListPost.address" +
            "")
    List<PostModel> getListPostByRenter(String renterId, Boolean isVisible, Boolean banned);
}
