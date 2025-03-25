package org.ll.ai_recommendation.domain.tool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ll.ai_recommendation.domain.category.subCategory.entity.SubCategory;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

@Entity
@Table(name = "tool_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolCategory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SubCategory subCategory;
}
