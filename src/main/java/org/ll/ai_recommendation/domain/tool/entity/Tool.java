package org.ll.ai_recommendation.domain.tool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.ll.ai_recommendation.global.baseEntity.BaseEntity;

import java.time.LocalDateTime;
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

    @Column(name = "icon_url")
    private String iconUrl;
    
    @Column(name = "is_verified")
    private boolean verified;
    
    @Column(name = "category_rank")
    private Integer categoryRank;
    
    @Column(name = "likes_count")
    private Integer likesCount;
    
    @Column(name = "is_paid")
    private boolean paid;
    
    @Column(name = "rating")
    private Double rating;
    
    @Column(name = "view_count")
    private Integer viewCount;
    
    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "tool_link")
    private String toolLink;

    @OneToMany(mappedBy = "tool")
    private List<ToolCategory> toolCategories;
}
