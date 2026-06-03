package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "wf_approval_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WfApprovalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "biz_type", nullable = false, length = 30)
    private String bizType;

    @Column(name = "biz_id", nullable = false)
    private Long bizId;

    @Column(name = "approver_id", nullable = false)
    private Long approverId;

    @Column(name = "action", nullable = false, length = 20)
    private String action;

    @Column(name = "opinion", length = 500)
    private String opinion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private String approverName;
}
