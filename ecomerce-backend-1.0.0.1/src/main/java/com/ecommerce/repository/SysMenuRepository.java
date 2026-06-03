package com.ecommerce.repository;

import com.ecommerce.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {
    List<SysMenu> findByParentIdOrderBySortOrderAsc(Long parentId);
}