package com.ticketPing.queue_manage.application.scheduler;

import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenTransferScheduler {

    private final WorkingQueueApplicationService workingQueueService;

    /**
     * 대기열 -> 작업열 이동
     */
    @Scheduled(fixedRateString = "${scheduler.queue-transfer-rate}")
    @SchedulerLock(name = "processScheduledQueueTransfer", lockAtMostFor = "5s", lockAtLeastFor = "5s")
    public void processScheduledQueueTransfer() {
        workingQueueService.processQueueTransfer();
    }

}
