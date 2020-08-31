package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, String> {

    @Query(value = "select new PostModel(p.id, p.price, p.distance, p.square, p.roomNumber, " +
            "p.description, p.title, p.address, p.visible, p.banned, " +
            "p.mapLocation, p.createDate, p.expireDate, " +
            "t.id, t.name, ll.username, ll.displayName, " +
            "sum(case when (r.statusReport.id = 3 or r.statusReport.id = 5) then 1 else 0 end) )" +
            " from PostModel p " +
            "join TypeModel t on p.type.id = t.id " +
            "join LandlordModel ll on p.landlord.username = ll.username " +
            "left outer join ReportModel r on p.id = r.postReport.id " +
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
            "and (:currentDate is null or p.expireDate < :currentDate)" +
            "group by p.id, p.price, p.distance, p.square, p.roomNumber, " +
            "p.description, p.title, p.address, p.visible, p.banned, p.mapLocation, p.createDate, " +
            "p.expireDate, t.id, t.name, ll.username, ll.displayName, ll.phoneNumber" +
            "")
    Page<PostModel> searchPost(String landlordId, String title, Double priceMax, Double priceMin,
                               Double distanceMax, Double distanceMin,
                               Double squareMax, Double squareMin, Boolean isVisible, Long postType, Boolean banned, Date currentDate, Pageable pageable);

    @Query(value = "select new PostModel(p.id, p.price, p.distance, p.square, " +
            "p.description, p.title, p.address, MAX (im.id), " +
            "sum(case when (r.status.id = 1) then 1 else 0 end) ) from PostModel p " +
            "join ImageModel im on p.id = im.post.id " +
            "left outer join RoomModel r on r.postRoom.id = p.id " +
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

    @Query(value = "select new PostModel(p.id, p.price, p.distance, p.square, p.roomNumber, " +
            "p.description, p.title, p.address, p.visible, p.banned, " +
            "p.mapLocation, p.createDate, p.expireDate, " +
            "t.id, t.name, ll.username, ll.displayName, ll.phoneNumber, " +
            "sum(case when (r.status.id = 1) then 1 else 0 end) ) from PostModel p " +
            "join TypeModel t on p.type.id = t.id " +
            "join LandlordModel ll on p.landlord.username = ll.username " +
            "left outer join RoomModel r on r.postRoom.id = p.id " +
            "where (:postId is null or p.id = :postId)" +
            "group by p.id, p.price, p.distance, p.square, p.roomNumber, p.description, p.title, p.address, " +
            "p.visible, p.banned, p.mapLocation, p.createDate, p.expireDate, t.id, t.name, ll.username, " +
            "ll.displayName, ll.phoneNumber" +
            "")
    PostModel getPostById(String postId);

    @Query(value = "select new PostModel(p.id, p.roomNumber, " +
            "p.title, p.visible, p.banned, " +
            "p.createDate, p.expireDate) from PostModel p " +
            "where (:landlordId is null or p.landlord.username = :landlordId)" +
            "")
    Page<PostModel> getPostsByLandlord(String landlordId, Pageable pageable);


//    @Query(value = "select top 5 * from POST p " +
//            "where (:landlordId is null or p.LANDLORD_ID like %:landlordId%)" +
//            "and (:visible is null or p.IS_VISIBLE = :visible)" +
//            "and (:banned is null or p.IS_BANNED = :banned)" +
//            "and ((p.ID != :postId)" +
//            "or (p.TYPE_ID = :typeId and p.ID != :postId))" +
//            "", nativeQuery = true)
//    List<PostModel> getRelatedPost(String postId, String landlordId,
//                                   Long typeId, Boolean visible, Boolean banned);

    @Query(value = "select new PostModel(p.id, p.price, p.title, MAX (im.id)) " +
            "from PostModel p " +
            "join ImageModel im on p.id = im.post.id " +
            "where 1 = 1 " +
            "and (:visible is null or p.visible = :visible)" +
            "and (:banned is null or p.banned = :banned) " +
            "and (:currentDate is null or p.expireDate >= :currentDate)" +
            "and (p.id <> :postId) " +
            "and ((:landlordId is null or p.landlord.username like %:landlordId%)" +
            "or (p.type.id = :typeId)) " +
            "group by p.id, p.price, p.title " +
            "")
    List<PostModel> getRelatedPost(String postId, String landlordId, Long typeId, Boolean visible,
                                   Boolean banned, Date currentDate, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update PostModel p " +
            "set p.visible = :visible " +
            "where p.id = :postId ")
    void updateVisiblePost(Boolean visible, String postId);

    @Query(value = "select new PostModel(wl.wishListPost.id) from WishListModel wl " +
            "join ImageModel im on wl.wishListPost.id = im.post.id " +
            "where (:renterId is null or wl.wishListRenter.username = :renterId)" +
            "and (:isVisible is null or wl.wishListPost.visible = :isVisible)" +
            "and (:banned is null or wl.wishListPost.banned = :banned)" +
            "and (:currentDate is null or wl.wishListPost.expireDate >= :currentDate)" +
            "group by wl.wishListPost.id" +
            "")
    List<PostModel> getListPostByRenter(String renterId, Boolean isVisible, Boolean banned, Date currentDate);

    @Query(value = "select new PostModel(p.id, p.expireDate) from PostModel p " +
            "where (:postId is null or p.id = :postId)" +
            "")
    PostModel getPostOnlyExpireDateById(String postId);

    @Transactional
    @Modifying
    @Query(value = "update PostModel p " +
            "set p.expireDate = :expireDate, " +
            "p.visible = :visible " +
            "where p.id = :postId ")
    void updateExpireDatePost(Date expireDate, Boolean visible, String postId);

    @Transactional
    @Modifying
    @Query(value = "update p " +
            "set p.TYPE_ID = :typeId, " +
            "p.PRICE = :price, " +
            "p.DISTANCE = :distance, " +
            "p.SQUARE = :square, " +
            "p.DESCRIPTION = :description, " +
            "p.TITLE = :title, " +
            "p.ADDRESS = :address, " +
            "p.MAP_LOCATION = :mapLocation " +
            "from POST p " +
            "inner join TYPE t on p.TYPE_ID = t.ID " +
            "where p.id = :postId ", nativeQuery = true)
    void updatePost(Long typeId, Double price, Double distance, Double square,
                    String description, String title, String address, String mapLocation, String postId);

    @Transactional
    @Modifying
    @Query(value = "delete p from POST p " +
            "where p.ID = :postId ", nativeQuery = true)
    void deletePostById(String postId);

    @Query(value = "select sum(pr.reportNumber) from " +
            "(select sum(case when (r.STATUS_ID = 3 or r.STATUS_ID = 4) then 1 else 0 end) reportNumber " +
            "from POST p " +
            "left outer join REPORT r on r.POST_ID = p.ID " +
            "where p.LANDLORD_ID = :landlordUsername " +
            "group by p.ID) as pr", nativeQuery = true)
    Long getReportNumberOfLandlord(String landlordUsername);
}
