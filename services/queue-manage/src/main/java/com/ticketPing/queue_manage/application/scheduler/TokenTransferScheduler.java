package com.ticketPing.queue_manage.application.scheduler;

import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenTransferScheduler {

    private final WorkingQueueApplicationService workingQueueService;

    /**
     * 대기열 -> 처리열 이동
     */
    @Scheduled(fixedRateString = "${scheduler.queue-transfer-rate}")
    public void processScheduledQueueTransfer() {
        workingQueueService.processQueueTransfer();
    }

}
