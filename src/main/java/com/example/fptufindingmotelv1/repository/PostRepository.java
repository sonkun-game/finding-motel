package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, String> {
    List<PostModel> findByVisibleTrue(Sort sort);

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
            "")
    List<PostModel> searchPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible, Long postType);

    @Query(value = "select p from PostModel p " +
            "where ((:landlordId is null or p.landlord.username like %:landlordId%) or (:title is null or p.title like %:title%))" +
            "and (:priceMax is null or p.price <= :priceMax) " +
            "and (:priceMin is null or p.price >= :priceMin) " +
            "and (:distanceMax is null or p.distance <= :distanceMax) " +
            "and (:distanceMin is null or p.distance >= :distanceMin) " +
            "and (:squareMax is null or p.square <= :squareMax) " +
            "and (:squareMin is null or p.square >= :squareMin) " +
            "and (:isVisible is null or p.visible = :isVisible)" +
            "")
    List<PostModel> searchPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible);
}
