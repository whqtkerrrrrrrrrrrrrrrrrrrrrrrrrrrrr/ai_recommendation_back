package org.ll.ai_recommendation.domain.tool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "tool")
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
    private List<ToolCategory> toolCategories;
}
