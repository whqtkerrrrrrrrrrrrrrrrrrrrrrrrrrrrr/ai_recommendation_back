package org.ll.ai_recommendation.domain.tool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

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

    @Column(name = "big_category")
    private String bigCategory;

    @Column(name = "small_category")
    private String smallCategory;
}
