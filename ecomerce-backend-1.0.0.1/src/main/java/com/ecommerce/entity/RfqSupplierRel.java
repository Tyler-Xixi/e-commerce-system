package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "rfq_supplier_rel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfqSupplierRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rfq_id", nullable = false)
    private Long rfqId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "is_selected", nullable = false)
    private Integer isSelected;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
