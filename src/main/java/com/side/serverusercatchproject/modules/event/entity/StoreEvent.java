package com.side.serverusercatchproject.modules.event.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreMenu;
import com.side.serverusercatchproject.modules.event.enums.StoreEventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ENTERPRISE_STORES_EVENTS_LIST")
public class StoreEvent extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("이벤트 메뉴 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    private EnterpriseStoreMenu menu;

    @Comment("이벤트 제목")
    private String title;

    @Enumerated(EnumType.STRING)
    @Comment("이벤트 활성화 상태")
    private StoreEventStatus status;

    public StoreEvent(EnterpriseStoreMenu menu, String title, StoreEventStatus status) {
        this.menu = menu;
        this.title = title;
        this.status = status;
    }
}
