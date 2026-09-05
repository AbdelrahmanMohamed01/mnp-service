package com.example.mnp.services;

import com.example.mnp.repository.PortingRequestRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TimeOutRequestsSchedulerService {
    private static final Logger log = LoggerFactory.getLogger(TimeOutRequestsSchedulerService.class);
    private static final int TIMEOUT_MINUTES = 2;
    private final PortingRequestRepository portingRequestRepository;
    public TimeOutRequestsSchedulerService(PortingRequestRepository portingRequestRepository) {
        this.portingRequestRepository=portingRequestRepository;
    }

    //10sec
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void cancelTimeOutPortingRequests(){
        Instant now = Instant.now();
        Instant timeoutThreshold = now.minus(TIMEOUT_MINUTES, ChronoUnit.MINUTES);

        int canceledCount = portingRequestRepository.cancelTimedOutPendingRequests(timeoutThreshold, now);
        if (canceledCount > 0) {
            log.info("background job: cancelled {} porting requests", canceledCount);
        }
    }
}
