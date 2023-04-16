package com.side.serverusercatchproject.modules.enterprise.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.MenuStatus;
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
@Table(name = "ENTERPRISE_STORE_MENUS_LIST")
public class EnterpriseStoreMenu extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @Comment("매장 정보")
    private EnterpriseStoreInfo store;
    
    @Comment("메뉴 이름")
    private String name;

    @Comment("메뉴 가격")
    private Integer price;

    @Comment("매뉴 사진")
    @OneToOne(cascade = CascadeType.ALL)
    private FileInfo fileInfo;

    @Comment("메뉴 설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Comment("메뉴 활성화 상태")
    private MenuStatus status;

    @Builder
    public EnterpriseStoreMenu(EnterpriseStoreInfo store, String name, Integer price, FileInfo fileInfo, String description, MenuStatus status) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.fileInfo = fileInfo;
        this.description = description;
        this.status = status;
    }
}
