package com.ticketPing.queue_manage.domain.service;

import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.create;

import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueDomainServiceImpl implements WaitingQueueDomainService {

    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public WaitingQueueToken enterWaitingQueue(EnterWaitingQueueRequest request) {
        WaitingQueueToken token = create(request);
        waitingQueueRepository.enqueue(EnqueueCommand.create(token));
        return token;
    }

    @Override
    public WaitingQueueToken retrieveToken(UUID userId) {
        return waitingQueueRepository.retrieveToken(RetrieveTokenCommand.create(userId));
    }

}
