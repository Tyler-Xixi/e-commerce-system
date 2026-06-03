package com.ecommerce.repository;

import com.ecommerce.entity.WfApprovalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WfApprovalRecordRepository extends JpaRepository<WfApprovalRecord, Long> {
    List<WfApprovalRecord> findByBizTypeAndBizIdOrderByCreatedAtAsc(String bizType, Long bizId);
}