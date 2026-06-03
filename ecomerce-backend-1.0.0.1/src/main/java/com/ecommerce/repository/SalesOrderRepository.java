package com.ecommerce.repository;

import com.ecommerce.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    Optional<SalesOrder> findByCode(String code);
}