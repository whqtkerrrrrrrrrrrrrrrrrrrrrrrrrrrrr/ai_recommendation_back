package org.ll.ai_recommendation.domain.tools.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ll.ai_recommendation.domain.tools.service.ToolsService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ToolsScheduler {
    private final ToolsService toolsService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void updateCareCenter() {
        log.info("Crawling start . . .");
        toolsService.crawlAndSaveTools();
        log.info("Crawling end . . .");
    }
}
