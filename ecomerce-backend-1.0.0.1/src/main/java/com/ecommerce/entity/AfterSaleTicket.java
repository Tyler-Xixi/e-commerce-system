package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "after_sale_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AfterSaleTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "order_no", length = 100)
    private String orderNo;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "owner", length = 50)
    private String owner;

    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "status", length = 20)
    private String status;
}
