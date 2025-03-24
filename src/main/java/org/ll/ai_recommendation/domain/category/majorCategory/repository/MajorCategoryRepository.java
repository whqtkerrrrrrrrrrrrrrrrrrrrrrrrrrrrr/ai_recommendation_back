package org.ll.ai_recommendation.domain.category.majorCategory.repository;

import org.ll.ai_recommendation.domain.category.majorCategory.entity.MajorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorCategoryRepository extends JpaRepository<MajorCategory, Long> {
}
