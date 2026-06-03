package com.ecommerce.repository;

import com.ecommerce.entity.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LogisticsRepository extends JpaRepository<Logistics, Long> {
    Optional<Logistics> findByCode(String code);
}