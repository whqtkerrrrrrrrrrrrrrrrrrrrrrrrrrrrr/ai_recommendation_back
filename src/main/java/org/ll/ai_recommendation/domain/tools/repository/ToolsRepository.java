package org.ll.ai_recommendation.domain.tools.repository;

import org.ll.ai_recommendation.domain.tools.entity.Tools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolsRepository extends JpaRepository<Tools, Long> {
    boolean existsByToolName(String toolName);
}
