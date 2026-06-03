package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_quotation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqQuotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rfq_no", unique = true, nullable = false, length = 50)
    @JsonAlias("rfq_no")
    private String rfqNo;

    @Column(name = "demand_id")
    @JsonAlias("demand_id")
    private Long demandId;

    @Column(name = "category_code", nullable = false, length = 50)
    @JsonAlias("category_code")
    private String categoryCode;

    @Column(name = "quantity", nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @Column(name = "specification", columnDefinition = "text")
    private String specification;

    @Column(name = "delivery_date", nullable = false)
    @JsonAlias("delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "payment_terms", length = 200)
    @JsonAlias("payment_terms")
    private String paymentTerms;

    @Column(name = "quote_deadline", nullable = false)
    @JsonAlias("quote_deadline")
    private LocalDateTime quoteDeadline;

    @Column(name = "source_type", nullable = false, length = 20)
    @JsonAlias("source_type")
    private String sourceType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "attachment", length = 500)
    private String attachment;

    @Column(name = "created_by", nullable = false)
    @JsonAlias("created_by")
    private Long createdBy;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "created_at")
    @JsonAlias("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonAlias("updated_at")
    private LocalDateTime updatedAt;
}
