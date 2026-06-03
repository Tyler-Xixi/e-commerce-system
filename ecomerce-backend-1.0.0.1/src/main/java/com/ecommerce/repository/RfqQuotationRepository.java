package com.ecommerce.repository;

import com.ecommerce.entity.RfqQuotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RfqQuotationRepository extends JpaRepository<RfqQuotation, Long> {
    Optional<RfqQuotation> findByRfqNo(String rfqNo);
    Page<RfqQuotation> findByStatusNot(String status, Pageable pageable);
}
