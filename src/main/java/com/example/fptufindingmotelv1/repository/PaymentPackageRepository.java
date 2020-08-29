package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentPackageRepository extends JpaRepository<PaymentPackageModel, Long> {
    @Query(value = "select new PaymentPackageModel(p.id, p.amount, p.duration, " +
                    "p.packageName, p.available) from PaymentPackageModel p " +
                    "where (1 = 1)" +
                    "and (:available is null or p.available = :available)" +
                    "order by p.duration")
    List<PaymentPackageModel> getListPaymentPackage(Boolean available);

    @Query(value = "select new PaymentPackageModel(p.id, p.amount, p.duration, " +
            "p.packageName, p.available) from PaymentPackageModel p " +
            "where (1 = 1)" +
            "and (:packageId is null or p.id = :packageId)")
    PaymentPackageModel getPackageById(Long packageId);

    @Query(value = "select new PaymentPackageModel(p.id, p.amount, p.duration, " +
            "p.packageName, p.available) from PaymentPackageModel p " +
            "order by p.duration")
    Page<PaymentPackageModel> getAllPaymentPackage(Pageable pageable);
}
