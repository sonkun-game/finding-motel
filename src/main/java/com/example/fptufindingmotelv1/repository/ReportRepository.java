package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.ReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<ReportModel, String> {
    @Query(value = "select rp from ReportModel rp " +
            "where 1 = 1" +
            "and (:landlordId is null or rp.postReport.landlord.username like %:landlordId%)" +
            "and (:renterId is null or rp.renterReport.username like %:renterId%)" +
            "and (:postTitle is null or rp.postReport.title like %:postTitle%)" +
            "and (:statusId is null or rp.statusReport.id = :statusId) " +
            "")
    Page<ReportModel> searchReport(String landlordId, String renterId, String postTitle, Long statusId, Pageable pageable);
}
