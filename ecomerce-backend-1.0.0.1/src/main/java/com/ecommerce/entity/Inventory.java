package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", length = 100)
    private String sku;

    @Column(name = "warehouse", length = 100)
    private String warehouse;

    @Column(name = "available")
    private Integer available;

    @Column(name = "locked")
    private Integer locked;

    @Column(name = "alert")
    private Integer alert;

    @Column(name = "status", length = 20)
    private String status;
}
