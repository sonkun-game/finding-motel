package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {
    @Query(value = "select new PaymentModel (p.id, p.amount, p.payDate, p.momoId, " +
            "p.paymentMethod, p.note) from PaymentModel p" +
            " where (1 = 1)" +
            "and (:landlordId is null or p.landlordModel.username = :landlordId)" +
            "order by p.payDate desc ")
    List<PaymentModel> getPaymentByLandlord(String landlordId);
}
