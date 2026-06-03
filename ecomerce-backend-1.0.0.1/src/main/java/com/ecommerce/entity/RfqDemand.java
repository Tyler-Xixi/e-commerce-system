package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_demand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqDemand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "demand_no", unique = true, nullable = false, length = 50)
    private String demandNo;

    @Column(name = "category_code", nullable = false, length = 50)
    private String categoryCode;

    @Column(name = "quantity", nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @Column(name = "specification", columnDefinition = "text")
    private String specification;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private String assigneeName;

    @Transient
    private String creatorName;
}
