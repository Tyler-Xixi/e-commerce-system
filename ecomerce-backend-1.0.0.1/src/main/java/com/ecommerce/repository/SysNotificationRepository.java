package com.ecommerce.repository;

import com.ecommerce.entity.SysNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SysNotificationRepository extends JpaRepository<SysNotification, Long> {
    List<SysNotification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
    Page<SysNotification> findByRecipientId(Long recipientId, Pageable pageable);
}
