package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, length = 50)
    private String code;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "contact", length = 50)
    private String contact;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "score")
    private Integer score;

    @Column(name = "status", length = 20)
    private String status;
}
