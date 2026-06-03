package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(name = "path", length = 200)
    private String path;

    @Column(name = "component", length = 200)
    private String component;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "visible")
    private Integer visible;

    @Column(name = "permission", length = 200)
    private String permission;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
