package org.ll.ai_recommendation.domain.tool.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ll.ai_recommendation.domain.category.majorCategory.entity.MajorCategory;
import org.ll.ai_recommendation.domain.category.subCategory.entity.SubCategory;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "ai_tools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tool extends BaseEntity {
    @Column(name = "tool_name", unique = true)
    private String toolName;

    @Column(name = "tool_description")
    private String toolDescription;

    @Column(name = "tool_link")
    private String toolLink;

    @OneToMany(mappedBy = "tool")
    private List<SubCategory> subCategories;

    @OneToOne(mappedBy = "tool")
    @JoinColumn(name = "major_category_id")
    private MajorCategory majorCategory;
}
