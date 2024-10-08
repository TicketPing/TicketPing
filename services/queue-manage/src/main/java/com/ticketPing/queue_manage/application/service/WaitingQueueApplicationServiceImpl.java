package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.create;

import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.dto.WaitingQueueInfoResponse;
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
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public EnterWaitingQueueResponse enterWaitingQueue(EnterWaitingQueueRequest request) {
        WaitingQueueToken token = create(request);
        waitingQueueRepository.enqueue(EnqueueCommand.create(token));
        return EnterWaitingQueueResponse.from(token);
    }

    @Override
    public WaitingQueueInfoResponse getWaitingQueueInfo(UUID userId) {
        WaitingQueueToken token = waitingQueueRepository.retrieveToken(RetrieveTokenCommand.create(userId));
        return WaitingQueueInfoResponse.from(token);
    }

}
