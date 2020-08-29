package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentPostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentPostRepository extends JpaRepository<PaymentPostModel, String> {
    @Query(value = "select new PaymentPostModel (p.id, p.payDate, pp.id, pp.amount, " +
            "pp.duration, pp.packageName, post.id, post.title) from PaymentPostModel p " +
            "join PaymentPackageModel pp on p.paymentPackage.id = pp.id " +
            "join PostModel post on p.postPayment.id = post.id " +
            "join LandlordModel ll on post.landlord.username = ll.username " +
            "where (1 = 1)" +
            "and (:landlordId is null or ll.username = :landlordId)" )
    Page<PaymentPostModel> getPaymentPostByLandlord(String landlordId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete pm from PAYMENT_POST pm " +
            "where pm.POST_ID = :postId ", nativeQuery = true)
    void deletePaymentPostsByPost(String postId);
}
