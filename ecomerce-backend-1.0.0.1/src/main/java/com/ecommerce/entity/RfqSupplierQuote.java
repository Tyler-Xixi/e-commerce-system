package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_supplier_quote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqSupplierQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rfq_id", nullable = false)
    private Long rfqId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "quote_type", nullable = false, length = 20)
    private String quoteType;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;

    @Column(name = "warranty_period", length = 100)
    private String warrantyPeriod;

    @Column(name = "payment_terms", length = 200)
    private String paymentTerms;

    @Column(name = "batch_price_gradient", columnDefinition = "text")
    private String batchPriceGradient;

    @Column(name = "cost_breakdown", columnDefinition = "text")
    private String costBreakdown;

    @Column(name = "attachment", length = 500)
    private String attachment;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Transient
    private String supplierName;

    @Transient
    private String rfqNo;
}
