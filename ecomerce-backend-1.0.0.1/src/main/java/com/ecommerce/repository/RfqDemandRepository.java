package com.ecommerce.repository;

import com.ecommerce.entity.RfqDemand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RfqDemandRepository extends JpaRepository<RfqDemand, Long> {
    Optional<RfqDemand> findByDemandNo(String demandNo);
    Page<RfqDemand> findByCreatedByOrAssigneeId(Long createdBy, Long assigneeId, Pageable pageable);
}
