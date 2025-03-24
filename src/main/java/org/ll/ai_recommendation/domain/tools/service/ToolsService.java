package org.ll.ai_recommendation.domain.tools.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ll.ai_recommendation.domain.tools.entity.Tools;
import org.ll.ai_recommendation.domain.tools.repository.ToolsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ToolsService {
    private final ToolsRepository toolsRepository;
    private static final String BASE_URL = "https://www.aixploria.com/en/categories-ai/";

    public void crawlAndSaveTools() {
        try {
            Document doc = Jsoup.connect(BASE_URL).get();
            Elements categoryCards = doc.select("div.categories-generales-grid div.categorie-generale-card");

            for (Element card : categoryCards) {
                String largeCategory = card.select("h2").attr("data-title");
                Elements categoriesList = card.select("ul.categories-associees li");

                for (Element item : categoriesList) {
                    Element aTag = item.select("a").first();
                    if (aTag != null) {
                        Element spanTag = aTag.select("span").first();
                        if (spanTag != null) {
                            String smallCategory = spanTag.attr("data-title");
                            String categoryUrl = aTag.attr("href");

                            crawlToolsFromCategory(categoryUrl, largeCategory, smallCategory);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("크롤링 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("크롤링 실패", e);
        }
    }

    private void crawlToolsFromCategory(String categoryUrl, String largeCategory, String smallCategory) {
        int page = 1;

        while (true) {
            try {
                String pageUrl = categoryUrl + "page/" + page + "/";
                Document doc = Jsoup.connect(pageUrl).get();

                Element latestPosts = doc.selectFirst("div[class='latest-posts']");
                if (latestPosts == null) break;

                Elements posts = latestPosts.select("div[class^=post-item]");
                if (posts.isEmpty()) break;

                for (Element post : posts) {
                    Element titleElement = post.selectFirst("span.post-title a.dark-title");
                    Element visitButton = post.selectFirst("a.visit-site-button4");

                    if (titleElement != null && visitButton != null) {
                        String toolName = titleElement.attr("title");
                        String toolUrl = visitButton.attr("href");

                        // 중복 체크 후 저장
                        if (!toolsRepository.existsByToolName(toolName)) {
                            Tools tool = Tools.builder()
                                    .toolName(toolName)
                                    .toolLink(toolUrl)
                                    .bigCategory(largeCategory)
                                    .smallCategory(smallCategory)
                                    .build();

                            toolsRepository.save(tool);
                            log.info("저장된 AI 도구: {}", toolName);
                        }
                    }
                }
                page++;
            } catch (IOException e) {
                log.error("페이지 크롤링 중 오류 발생: {}", e.getMessage());
                break;
            }
        }
    }
}
