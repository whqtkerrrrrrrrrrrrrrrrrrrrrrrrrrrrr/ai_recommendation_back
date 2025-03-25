package org.ll.ai_recommendation.domain.tool.controller;

import lombok.RequiredArgsConstructor;
import org.ll.ai_recommendation.domain.tool.service.ToolService;
import org.ll.ai_recommendation.global.globalDto.GlobalResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/crawling")
public class ApiV1ToolController {
    private final ToolService toolService;

    @GetMapping("/start")
    public GlobalResponse<Void> startCrawling() {
        long start = System.currentTimeMillis();
        toolService.saveCategories();
        toolService.saveTools();
        long end = System.currentTimeMillis();
        System.out.println("실행 시간: " + (end - start) / 1000.0 + "초");
        return GlobalResponse.success();
    }
}
