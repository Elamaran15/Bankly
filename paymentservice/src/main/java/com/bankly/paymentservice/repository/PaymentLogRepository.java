package com.bankly.paymentservice.repository;


import com.bankly.paymentservice.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, UUID> {

   Optional<PaymentLog> findByTransactionId(UUID transactionId);
}
