package com.side.serverusercatchproject.modules.enterprise.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.category.entity.Category;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreCategoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ENTERPRISE_STORE_CATEGORIES_LIST")
public class EnterpriseStoreCategory extends BaseTime {
    @Id
    @Comment("고유 번호")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @Comment("매장 정보")
    private EnterpriseStoreInfo store;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @Comment("등록 카테고리")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Comment("매장 카테고리 활성화 상태")
    private StoreCategoryStatus status;
}
