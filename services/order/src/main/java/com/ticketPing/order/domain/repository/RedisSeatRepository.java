package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.entity.RedisSeat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RedisSeatRepository extends CrudRepository<RedisSeat, UUID> {

    Optional<RedisSeat> findBySeatId(String SeatId);
}


