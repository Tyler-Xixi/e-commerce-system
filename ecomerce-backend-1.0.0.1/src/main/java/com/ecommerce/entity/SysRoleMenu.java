package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_role_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
