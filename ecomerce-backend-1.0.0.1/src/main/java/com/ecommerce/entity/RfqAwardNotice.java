package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_award_notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqAwardNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notice_no", unique = true, nullable = false, length = 50)
    private String noticeNo;

    @Column(name = "award_id", nullable = false)
    private Long awardId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "confirm_remark", length = 500)
    private String confirmRemark;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
