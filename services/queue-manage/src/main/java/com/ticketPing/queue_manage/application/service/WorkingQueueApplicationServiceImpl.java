package com.ticketPing.queue_manage.application.service;

import com.ticketPing.queue_manage.domain.service.WorkingQueueDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingQueueApplicationServiceImpl implements WorkingQueueApplicationService {

    private final WorkingQueueDomainService workingQueueDomainService;

    @Override
    public void processQueueTransfer() {
        workingQueueDomainService.processQueueTransfer();
    }

}
