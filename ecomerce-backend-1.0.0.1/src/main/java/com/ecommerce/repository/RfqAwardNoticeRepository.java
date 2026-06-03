package com.ecommerce.repository;

import com.ecommerce.entity.RfqAwardNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RfqAwardNoticeRepository extends JpaRepository<RfqAwardNotice, Long> {
    Optional<RfqAwardNotice> findByNoticeNo(String noticeNo);
}