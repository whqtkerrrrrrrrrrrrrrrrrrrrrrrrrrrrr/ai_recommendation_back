package org.ll.ai_recommendation.domain.category.subCategory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ll.ai_recommendation.domain.category.majorCategory.entity.MajorCategory;
import org.ll.ai_recommendation.domain.tool.entity.ToolCategory;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "sub_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategory extends BaseEntity {
    @Column(name = "sub_category_name", unique = true)
    String subCategoryName;

    @Column(name="sub_category_url")
    String subCategoryUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_category_id")
    MajorCategory majorCategory;

    @OneToMany(mappedBy = "subCategory")
    private List<ToolCategory> toolCategories;
}
