package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "logistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Logistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "order_no", length = 100)
    private String orderNo;

    @Column(name = "carrier", length = 100)
    private String carrier;

    @Column(name = "route", length = 200)
    private String route;

    @Column(name = "eta")
    private LocalDateTime eta;

    @Column(name = "status", length = 20)
    private String status;
}
