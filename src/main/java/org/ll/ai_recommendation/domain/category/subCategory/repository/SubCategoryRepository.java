package org.ll.ai_recommendation.domain.category.subCategory.repository;

import org.ll.ai_recommendation.domain.category.subCategory.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findBySubCategoryName(String subCategoryName);
}
