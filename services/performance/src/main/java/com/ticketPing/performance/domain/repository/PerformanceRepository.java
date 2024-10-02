package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Performance;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, UUID> {

}
