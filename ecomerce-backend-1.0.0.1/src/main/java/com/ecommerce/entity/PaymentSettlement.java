package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "payment_settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "order_no", length = 100)
    private String orderNo;

    @Column(name = "channel", length = 100)
    private String channel;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "account_date", length = 20)
    private String accountDate;

    @Column(name = "status", length = 20)
    private String status;
}
