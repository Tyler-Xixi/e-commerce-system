package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_business_award")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqBusinessAward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "award_no", unique = true, nullable = false, length = 50)
    private String awardNo;

    @Column(name = "rfq_id", nullable = false)
    private Long rfqId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "analysis_report", length = 500)
    private String analysisReport;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private String supplierName;

    @Transient
    private String rfqNo;
}
