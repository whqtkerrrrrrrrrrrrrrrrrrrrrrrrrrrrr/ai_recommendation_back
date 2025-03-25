package org.ll.ai_recommendation.domain.tool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ll.ai_recommendation.domain.category.majorCategory.entity.MajorCategory;
import org.ll.ai_recommendation.domain.category.majorCategory.repository.MajorCategoryRepository;
import org.ll.ai_recommendation.domain.category.subCategory.entity.SubCategory;
import org.ll.ai_recommendation.domain.category.subCategory.repository.SubCategoryRepository;
import org.ll.ai_recommendation.domain.tool.entity.Tool;
import org.ll.ai_recommendation.domain.tool.entity.ToolCategory;
import org.ll.ai_recommendation.domain.tool.repository.ToolRepository;
import org.ll.ai_recommendation.domain.tool.repository.ToolCategoryRepository; // 추가
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ToolService {
    private final ToolRepository toolsRepository;
    private final MajorCategoryRepository majorCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ToolCategoryRepository toolCategoryRepository;

    // 크롤링 대상 URL
    private static final String BASE_URL = "https://www.aixploria.com/en/categories-ai/";

    public void saveCategories() {
        try {
            Document doc = Jsoup.connect(BASE_URL).get();
            Elements categoryCards = doc.select("div.categories-generales-grid div.categorie-generale-card");

            // 모든 대분류 카테고리 저장
            List<MajorCategory> majorCategories = new ArrayList<>();
            for (Element card : categoryCards) {
                String majorCategoryName = card.select("h2").attr("data-title");
                
                // 대분류 카테고리 생성 및 저장
                MajorCategory majorCategory = MajorCategory.builder()
                        .majorCategoryName(majorCategoryName)
                        .subCategories(new ArrayList<>())
                        .build();
                
                majorCategories.add(majorCategoryRepository.save(majorCategory));
                log.info("대분류 카테고리 저장: {}", majorCategoryName);
            }

            // 각 대분류 카테고리에 대한 소분류 카테고리 저장
            for (int i = 0; i < categoryCards.size(); i++) {
                Element card = categoryCards.get(i);
                MajorCategory majorCategory = majorCategories.get(i);
                
                Elements categoriesList = card.select("ul.categories-associees li");
                
                for (Element item : categoriesList) {
                    Element aTag = item.select("a").first();
                    if (aTag != null) {
                        Element spanTag = aTag.select("span").first();
                        
                        if (spanTag != null) {
                            String subCategoryName = spanTag.attr("data-title");
                            String categoryUrl = aTag.attr("href");

                            // 소분류 카테고리 생성 및 저장
                            SubCategory subCategory = SubCategory.builder()
                                    .subCategoryName(subCategoryName)
                                    .subCategoryUrl(categoryUrl)
                                    .majorCategory(majorCategory)
                                    .build();
                            
                            subCategoryRepository.save(subCategory);
                            log.info("소분류 카테고리 저장: {} (대분류: {})", subCategoryName, majorCategory.getMajorCategoryName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("카테고리 크롤링 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("카테고리 크롤링 실패", e);
        }
    }

    public void saveTools() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        
        for (SubCategory subCategory : subCategories) {
            String categoryUrl = subCategory.getSubCategoryUrl();
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

                        if (titleElement != null) {
                            String detailUrl = titleElement.attr("href");

                            try {
                                Document detailDoc = Jsoup.connect(detailUrl).get();

                                String toolName = detailDoc.selectFirst("span[class*=post-title]")
                                        .text();

                                String description = detailDoc.selectFirst("span.desc-text")
                                        .text();

                                if (!toolsRepository.existsByToolName(toolName)) {
                                    Tool tool = Tool.builder()
                                            .toolName(toolName)
                                            .toolDescription(description)
                                            .toolLink(detailUrl)
                                            .toolCategories(new ArrayList<>())
                                            .build();

                                    Tool savedTool = toolsRepository.save(tool);

                                    Elements categoryElements = detailDoc.select("div.entry-categories a span[data-title]");
                                    for (Element categoryElement : categoryElements) {
                                        String categoryName = categoryElement.attr("data-title").trim();
                                        
                                        SubCategory subCat = subCategoryRepository.findBySubCategoryName(categoryName)
                                                .orElse(null);

                                        if (subCat != null) {
                                            ToolCategory toolCategory = ToolCategory.builder()
                                                    .tool(savedTool)
                                                    .subCategory(subCat)
                                                    .build();

                                            savedTool.getToolCategories().add(toolCategory);
                                            if (subCat.getToolCategories() == null) {
                                                subCat.setToolCategories(new ArrayList<>());
                                            }
                                            subCat.getToolCategories().add(toolCategory);

                                            toolCategoryRepository.save(toolCategory);
                                            
                                            log.info("도구-카테고리 연결: {} - {}", toolName, categoryName);
                                        }
                                    }
                                    
                                    log.info("저장된 AI 도구: {}", toolName);
                                }

                            } catch (IOException e) {
                                log.error("도구 상세 페이지 크롤링 중 오류 발생: {}", e.getMessage());
                                continue;
                            }
                        }
                    }
                    page++;
                } catch (IOException e) {
                    log.error("페이지 크롤링 중 오류 발생: {} (카테고리: {})", e.getMessage(), subCategory.getSubCategoryName());
                    break;
                }
            }
        }
    }
}
