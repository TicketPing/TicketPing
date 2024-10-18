package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.model.entity.RedisSeat;
import org.springframework.data.repository.CrudRepository;

public interface RedisSeatRepository extends CrudRepository<RedisSeat, String> {

}



