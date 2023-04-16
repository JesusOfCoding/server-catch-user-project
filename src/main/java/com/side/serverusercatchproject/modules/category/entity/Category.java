package com.side.serverusercatchproject.modules.category.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.category.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CATEGORIES")
public class Category extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("카테고리 이름")
    private String name;

    @Comment("카테고리 활성화 상태")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @Builder
    public Category(String name, CategoryStatus status) {
        this.name = name;
        this.status = status;
    }
}
