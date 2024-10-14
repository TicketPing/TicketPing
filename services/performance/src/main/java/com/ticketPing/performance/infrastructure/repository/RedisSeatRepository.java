package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.entity.RedisSeat;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RedisSeatRepository extends CrudRepository<RedisSeat, UUID> {
}
