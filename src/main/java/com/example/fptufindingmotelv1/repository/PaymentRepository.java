package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {
}
