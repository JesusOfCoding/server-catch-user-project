package com.side.serverusercatchproject.modules.banner.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BANNER_ENTERPRISE_LIST")
public class BannerEnterprise extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("배너 탭 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banner_sort_id")
    private BannerSort bannerSort;

    @Comment("매장 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private EnterpriseStoreInfo store;

    @Builder
    public BannerEnterprise(BannerSort bannerSort, EnterpriseStoreInfo store) {
        this.bannerSort = bannerSort;
        this.store = store;
    }
}
