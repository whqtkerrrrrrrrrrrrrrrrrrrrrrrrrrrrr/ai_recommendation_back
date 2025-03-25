package org.ll.ai_recommendation.domain.tool.repository;

import org.ll.ai_recommendation.domain.tool.entity.ToolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolCategoryRepository extends JpaRepository<ToolCategory, Long> {
}
