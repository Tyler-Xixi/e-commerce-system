package com.ecommerce.repository;

import com.ecommerce.entity.RfqBusinessAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RfqBusinessAwardRepository extends JpaRepository<RfqBusinessAward, Long> {
    Optional<RfqBusinessAward> findByAwardNo(String awardNo);
    Page<RfqBusinessAward> findBySupplierId(Long supplierId, Pageable pageable);
}
