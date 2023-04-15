package com.side.serverusercatchproject.modules.enterprise.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ENTERPRISE_STORE_INFO_LIST")
public class EnterpriseStoreInfo extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @Comment("기업 정보")
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @Comment("매장 이름")
    private String name;

    @Comment("매장 주소")
    private String address;

    @Comment("예약 선금")
    private Integer reservationPrice;

    @Comment("예약 간격")
    private String reservationTerm;

    @Comment("예약 취소 가능 날짜")
    private LocalDateTime reservationCancelDay;

    @Comment("위도")
    private Double lat;

    @Comment("경도")
    private Double lon;

    @Comment("매장 사진 정보")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;

    @Enumerated(EnumType.STRING)
    @Comment("매장 활성화 상태")
    private StoreStatus status;

    @Builder
    public EnterpriseStoreInfo(Enterprise enterprise, String name, String address, Integer reservationPrice
            , String reservationTerm, LocalDateTime reservationCancelDay, Double lat, Double lon, FileInfo fileInfo, StoreStatus status) {
        this.enterprise = enterprise;
        this.name = name;
        this.address = address;
        this.reservationPrice = reservationPrice;
        this.reservationTerm = reservationTerm;
        this.reservationCancelDay = reservationCancelDay;
        this.lat = lat;
        this.lon = lon;
        this.fileInfo = fileInfo;
        this.status = status;
    }
}
