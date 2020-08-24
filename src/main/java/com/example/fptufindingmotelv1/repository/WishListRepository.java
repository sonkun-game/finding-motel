package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishListModel, String> {
    List<WishListModel> findAllByWishListRenter(RenterModel renter, Sort sort);

    WishListModel findByWishListPostAndWishListRenter(PostModel post, RenterModel renter);

    @Transactional
    @Modifying
    @Query(value = "delete wl from WISH_LIST wl " +
            "where wl.POST_ID = :postId ", nativeQuery = true)
    void deleteWishListsByPost(String postId);
}
