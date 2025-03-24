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
        toolService.saveCategories();
        toolService.saveTools();
        return GlobalResponse.success();
    }
}
