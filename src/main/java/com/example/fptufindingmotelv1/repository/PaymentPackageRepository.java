package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentPackageRepository extends JpaRepository<PaymentPackageModel, Long> {
    @Query(value = "select p from PaymentPackageModel p " +
                    "where (1 = 1)" +
                    "and (:available is null or p.available = :available)" +
                    "order by p.duration")
    List<PaymentPackageModel> getListPaymentPackage(Boolean available);

    @Query(value = "select p from PaymentPackageModel p " +
            "order by p.duration")
    Page<PaymentPackageModel> getAllPaymentPackage(Pageable pageable);
}
