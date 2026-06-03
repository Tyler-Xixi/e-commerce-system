package com.ecommerce.repository;

import com.ecommerce.entity.RfqSupplierRel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RfqSupplierRelRepository extends JpaRepository<RfqSupplierRel, Long> {
    List<RfqSupplierRel> findByRfqId(Long rfqId);
}