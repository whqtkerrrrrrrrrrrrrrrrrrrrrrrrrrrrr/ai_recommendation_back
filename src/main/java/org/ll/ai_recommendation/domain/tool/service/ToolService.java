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
import org.ll.ai_recommendation.domain.tool.repository.ToolRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ToolService {
    private final ToolRepository toolsRepository;
    private final MajorCategoryRepository majorCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    // 크롤링 대상 URL
    private static final String BASE_URL = "https://www.aixploria.com/en/categories-ai/";

    public void saveCategories() {
        try {
            Document doc = Jsoup.connect(BASE_URL).get();
            Elements categoryCards = doc.select("div.categories-generales-grid div.categorie-generale-card");

            // 1. 모든 대분류 카테고리 저장
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

            // 2. 각 대분류 카테고리에 대한 소분류 카테고리 저장
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
                                    .majorCategory(majorCategory)  // 대분류 연관관계 설정
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
//        int page = 1;
//
//        while (true) {
//            try {
//                String pageUrl = categoryUrl + "page/" + page + "/";
//                Document doc = Jsoup.connect(pageUrl).get();
//
//                Element latestPosts = doc.selectFirst("div[class='latest-posts']");
//                if (latestPosts == null) break;
//
//                Elements posts = latestPosts.select("div[class^=post-item]");
//                if (posts.isEmpty()) break;
//
//                for (Element post : posts) {
//                    Element titleElement = post.selectFirst("span.post-title a.dark-title");
//
//                    if (titleElement != null) {
//                        String detailUrl = titleElement.attr("href");
//
//                        try {
//                            Document detailDoc = Jsoup.connect(detailUrl).get();
//
//                            String toolName = detailDoc.selectFirst("span.post-title-ticle")
//                                    .text();
//
//                            String description = detailDoc.selectFirst("span.desc-text")
//                                    .text();
//
//                            Element categoriesDiv = detailDoc.selectFirst("div.entry-categories");
//                            List<SubCategory> subCategories = new ArrayList<>();
//
//                            if (categoriesDiv != null) {
//                                Elements categoryElements = categoriesDiv.select("a");
//                                for (Element categoryElement : categoryElements) {
//                                    String categoryName = categoryElement.text().trim();
//
//                                    // 이미 존재하는 SubCategory 확인 또는 새로 생성
//                                    SubCategory subCategory = SubCategory.builder()
//                                            .subCategoryName(categoryName)
//                                            .build();
//
//                                    subCategories.add(subCategory);
//                                    subCategoryRepository.save(subCategory);
//                                }
//                            }
//
//                            // 중복 체크 후 저장
//                            if (!toolsRepository.existsByToolName(toolName)) {
//                                Tool tool = Tool.builder()
//                                        .toolName(toolName)
//                                        .toolDescription(description)
//                                        .toolLink(detailUrl)
//                                        .subCategories(subCategories)
//                                        .build();
//
//                                Tool savedTool = toolsRepository.save(tool);
//
//                                // SubCategory와 Tool 연관관계 설정
//                                for (SubCategory subCategory : subCategories) {
//                                    subCategory.setTool(savedTool);
//                                    subCategoryRepository.save(subCategory);
//                                }
//
//                                log.info("저장된 AI 도구: {}", toolName);
//                            }
//
//                        } catch (IOException e) {
//                            log.error("도구 상세 페이지 크롤링 중 오류 발생: {}", e.getMessage());
//                            continue;
//                        }
//                    }
//                }
//                page++;
//            } catch (IOException e) {
//                log.error("페이지 크롤링 중 오류 발생: {}", e.getMessage());
//                break;
//            }
//        }
    }
}
