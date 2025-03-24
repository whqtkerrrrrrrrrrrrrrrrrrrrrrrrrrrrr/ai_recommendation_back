package org.ll.ai_recommendation.domain.category.majorCategory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.ll.ai_recommendation.domain.category.subCategory.entity.SubCategory;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "major_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorCategory extends BaseEntity {
    @Column(name = "major_category_name", unique = true)
    private String majorCategoryName;

    @OneToMany(mappedBy = "majorCategory")
    private List<SubCategory> subCategories;
}
