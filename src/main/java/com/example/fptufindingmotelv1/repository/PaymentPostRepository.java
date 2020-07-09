package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.PaymentPostModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentPostRepository extends JpaRepository<PaymentPostModel, String> {
}
