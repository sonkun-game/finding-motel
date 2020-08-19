package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, String> {
//    List<PostModel> findByVisibleTrueAndBannedFalse(Sort sort);

    @Query(value = "select p from PostModel p " +
            "where ((:landlordId is null or p.landlord.username like %:landlordId%) or (:title is null or p.title like %:title%))" +
            "and (:priceMax is null or p.price <= :priceMax) " +
            "and (:priceMin is null or p.price >= :priceMin) " +
            "and (:distanceMax is null or p.distance <= :distanceMax) " +
            "and (:distanceMin is null or p.distance >= :distanceMin) " +
            "and (:squareMax is null or p.square <= :squareMax) " +
            "and (:squareMin is null or p.square >= :squareMin) " +
            "and (:isVisible is null or p.visible = :isVisible)" +
            "and (:postType is null or p.type.id = :postType) " +
            "and (:banned is null or p.banned = :banned)" +
            "")
    Page<PostModel> searchPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible, Long postType, Boolean banned, Pageable pageable);

    @Query(value = "select new PostModel(p.id, p.price, p.distance, p.square, " +
            "p.description, p.title, p.address, MAX (im.id)) from PostModel p " +
            "join ImageModel im on p.id = im.post.id " +
            "where ((:landlordId is null or p.landlord.username like %:landlordId%) or (:title is null or p.title like %:title%))" +
            "and (:priceMax is null or p.price <= :priceMax) " +
            "and (:priceMin is null or p.price >= :priceMin) " +
            "and (:distanceMax is null or p.distance <= :distanceMax) " +
            "and (:distanceMin is null or p.distance >= :distanceMin) " +
            "and (:squareMax is null or p.square <= :squareMax) " +
            "and (:squareMin is null or p.square >= :squareMin) " +
            "and (:isVisible is null or p.visible = :isVisible)" +
            "and (:postType is null or p.type.id = :postType) " +
            "and (:banned is null or p.banned = :banned)" +
            "and (:currentDate is null or p.expireDate >= :currentDate)" +
            "group by p.id, p.price, p.distance, p.square, p.description, p.title, p.address, p.createDate " +
            "order by p.createDate desc " +
            "")
    List<PostModel> filterPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible,
                               Long postType, Boolean banned, Date currentDate);

    @Query(value = "select new PostModel(p.id, p.price, p.distance, p.square, " +
            "p.description, p.title, p.address, p.visible, p.banned, " +
            "p.mapLocation, p.createDate, p.expireDate, " +
            "t.id, t.name, ll.username, ll.displayName, ll.phoneNumber) from PostModel p " +
            "join TypeModel t on p.type.id = t.id " +
            "join LandlordModel ll on p.landlord.username = ll.username " +
            "where (:postId is null or p.id = :postId)" +
            "")
    PostModel getPostById(String postId);


    @Query(value = "select top 5 * from POST p " +
            "where (:landlordId is null or p.LANDLORD_ID like %:landlordId%)" +
            "and (:visible is null or p.IS_VISIBLE = :visible)" +
            "and (:banned is null or p.IS_BANNED = :banned)" +
            "and ((p.ID != :postId)" +
            "or (p.TYPE_ID = :typeId and p.ID != :postId))" +
            "", nativeQuery = true)
    List<PostModel> getRelatedPost(String postId, String landlordId,
                                   Long typeId, Boolean visible, Boolean banned);

    @Transactional
    @Modifying
    @Query(value = "update PostModel p " +
            "set p.visible = :visible " +
            "where p.id = :postId ")
    void updateVisiblePost(Boolean visible, String postId);

    @Query(value = "select new PostModel(wl.wishListPost.id, wl.wishListPost.price, " +
            "wl.wishListPost.distance, wl.wishListPost.square, " +
            "wl.wishListPost.description, wl.wishListPost.title, " +
            "wl.wishListPost.address, MAX (im.id)) from WishListModel wl " +
            "join ImageModel im on wl.wishListPost.id = im.post.id " +
            "where (:renterId is null or wl.wishListRenter.username = :renterId)" +
            "and (:isVisible is null or wl.wishListPost.visible = :isVisible)" +
            "and (:banned is null or wl.wishListPost.banned = :banned)" +
            "and (:currentDate is null or wl.wishListPost.expireDate >= :currentDate)" +
            "group by wl.wishListPost.id, wl.wishListPost.price, wl.wishListPost.distance, " +
            "wl.wishListPost.square, wl.wishListPost.description, wl.wishListPost.title, wl.wishListPost.address" +
            "")
    List<PostModel> getListPostByRenter(String renterId, Boolean isVisible, Boolean banned, Date currentDate);
}
