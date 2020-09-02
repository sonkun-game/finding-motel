package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.ReportModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ReportRepository extends JpaRepository<ReportModel, String> {
    @Query(value = "select new ReportModel(rp.id, rp.content, rp.reportDate, p.id, p.title, " +
            "p.landlord.username, rp.renterReport.username, s.id, s.status) from ReportModel rp " +
            "join PostModel p on rp.postReport.id = p.id " +
            "join StatusModel s on rp.statusReport.id = s.id " +
            "where 1 = 1" +
            "and (:landlordId is null or rp.postReport.landlord.username like %:landlordId%)" +
            "and (:renterId is null or rp.renterReport.username like %:renterId%)" +
            "and (:postTitle is null or rp.postReport.title like %:postTitle%)" +
            "and (:statusId is null or rp.statusReport.id = :statusId) " +
            "")
    Page<ReportModel> searchReport(String landlordId, String renterId, String postTitle, Long statusId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete r from REPORT r " +
            "where r.POST_ID = :postId ", nativeQuery = true)
    void deleteReportsByPost(String postId);

    @Transactional
    @Modifying
    @Query(value = "update ReportModel rp " +
            "set rp.statusReport.id = case rp.statusReport.id " +
            "when 3 then :statusReportPost " +
            "when 4 then :statusReportAll " +
            "else rp.statusReport.id end " +
            "where rp.postReport.id = :postId " +
            "")
    void updateStatusReportByPost(String postId, Long statusReportPost, Long statusReportAll);

}
