package org.ll.ai_recommendation.domain.tools.controller;

import lombok.RequiredArgsConstructor;
import org.ll.ai_recommendation.domain.tools.service.ToolsService;
import org.ll.ai_recommendation.global.globalDto.GlobalResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/crawling")
public class ApiV1ToolsController {
    private final ToolsService toolsService;

    @GetMapping("/start")
    public GlobalResponse<Void> startCrawling() {
        toolsService.crawlAndSaveTools();
        return GlobalResponse.success();
    }
}
