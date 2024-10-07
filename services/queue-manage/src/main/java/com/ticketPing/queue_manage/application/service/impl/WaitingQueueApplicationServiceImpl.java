package com.ticketPing.queue_manage.application.service.impl;

import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.create;

import com.ticketPing.queue_manage.application.command.EnqueueCommand;
import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.service.WaitingQueueApplicationService;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueRepository repository;

    @Override
    public EnterWaitingQueueResponse enterWaitingQueue(EnterWaitingQueueRequest request) {
        WaitingQueueToken token = create(request);
        repository.enqueue(EnqueueCommand.create(token));
        return EnterWaitingQueueResponse.from(token);
    }

}
