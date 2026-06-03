package com.ecommerce.repository;

import com.ecommerce.entity.PaymentSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentSettlementRepository extends JpaRepository<PaymentSettlement, Long> {
    Optional<PaymentSettlement> findByCode(String code);
}