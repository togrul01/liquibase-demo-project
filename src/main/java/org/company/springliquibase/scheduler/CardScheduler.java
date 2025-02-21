package org.company.springliquibase.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.company.springliquibase.service.CardService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardScheduler {
    private final CardService cardService;

    @Scheduled(fixedDelayString = "PT1M")
    @SchedulerLock(name = "increaseCardBalances", lockAtLeastFor = "PT1M", lockAtMostFor = "PT3M")
    public void increaseCardBalances() {
        cardService.increaseCardBalances();
    }
}
