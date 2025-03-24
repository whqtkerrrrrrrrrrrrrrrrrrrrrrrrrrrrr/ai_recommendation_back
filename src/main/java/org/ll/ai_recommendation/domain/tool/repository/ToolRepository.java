package org.ll.ai_recommendation.domain.tool.repository;

import org.ll.ai_recommendation.domain.tool.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    boolean existsByToolName(String toolName);
}
