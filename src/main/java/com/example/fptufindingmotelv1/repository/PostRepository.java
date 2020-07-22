package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, String> {
    List<PostModel> findByVisibleTrueAndBannedFalse(Sort sort);

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
            "order by p.createDate desc " +
            "")
    List<PostModel> searchPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible, Long postType, Boolean banned);

    @Query(value = "select top 5 * from POST p " +
            "where (:landlordId is null or p.LANDLORD_ID like %:landlordId%)" +
            "and (p.ID != :postId)" +
            "or (p.TYPE_ID = :typeId and p.ID != :postId)" +
            "", nativeQuery = true)
    List<PostModel> getRelatedPost(String postId, String landlordId, Long typeId);

}
