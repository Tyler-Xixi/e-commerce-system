package com.ecommerce.repository;

import com.ecommerce.entity.RfqSupplierQuote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RfqSupplierQuoteRepository extends JpaRepository<RfqSupplierQuote, Long> {
    List<RfqSupplierQuote> findByRfqId(Long rfqId);
    Page<RfqSupplierQuote> findBySupplierId(Long supplierId, Pageable pageable);
}
