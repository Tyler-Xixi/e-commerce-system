package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "biz_type", length = 30)
    private String bizType;

    @Column(name = "biz_id")
    private Long bizId;

    @Column(name = "is_read", nullable = false)
    private Integer isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
