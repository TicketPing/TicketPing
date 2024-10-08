package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.dto.WaitingQueueInfoResponse;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.service.WaitingQueueDomainService;
import com.ticketPing.queue_manage.presentaion.request.EnterWaitingQueueRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueDomainService waitingQueueDomainService;

    @Override
    public EnterWaitingQueueResponse enterWaitingQueue(EnterWaitingQueueRequest request) {
        WaitingQueueToken token = waitingQueueDomainService.enterWaitingQueue(request);
        return EnterWaitingQueueResponse.from(token);
    }

    @Override
    public WaitingQueueInfoResponse getWaitingQueueInfo(UUID userId) {
        WaitingQueueToken token = waitingQueueDomainService.retrieveToken(userId);
        return WaitingQueueInfoResponse.from(token);
    }

}
