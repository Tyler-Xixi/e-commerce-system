package com.ecommerce.repository;

import com.ecommerce.entity.AfterSaleTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AfterSaleTicketRepository extends JpaRepository<AfterSaleTicket, Long> {
    Optional<AfterSaleTicket> findByCode(String code);
}