package com.side.serverusercatchproject.modules.enterprise.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
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
@Table(name = "ENTERPRISE_STORE_CALENDAR_LIST")
public class EnterpriseStoreCalendar extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유 번호")
    private Integer id;

    @Comment("매장 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    private EnterpriseStoreInfo store;
    
    @Comment("요일")
    @Column(name = "store_day")
    private String day;
    
    @Comment("영업 시작 시간")
    private LocalDateTime startTime;

    @Comment("영업 마감 시간")
    private LocalDateTime endTime;
    
    @Comment("브레이크 타임 시작 시간")
    private LocalDateTime startBreakTime;

    @Comment("브레이크 타임 마감 시간")
    private LocalDateTime endBreakTime;

    @Comment("영업 여부")
    private Boolean isOpen;

    @Builder
    public EnterpriseStoreCalendar(EnterpriseStoreInfo store, String day, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime startBreakTime, LocalDateTime endBreakTime, Boolean isOpen) {
        this.store = store;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startBreakTime = startBreakTime;
        this.endBreakTime = endBreakTime;
        this.isOpen = isOpen;
    }
}
