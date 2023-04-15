package com.side.serverusercatchproject.modules.reservation.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.reservation.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RESERVATIONS")
public class Reservation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("유저 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("예약 인원")
    private Integer qty;

    @Comment("매장 정보")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private EnterpriseStoreInfo store;

    @Comment("예약 간격")
    private String reservationTerm;

    @Comment("예약 취소 가능 날짜")
    private String reservationCancelDay;

    @Comment("예약 누른 시간")
    private LocalDateTime pushTime;

    @Comment("예약 완료 시간")
    private LocalDateTime activeTime;

    @Comment("예약 당시 선금")
    private Integer reservationPrice;

    @Enumerated(EnumType.STRING)
    @Comment("예약 상태")
    private ReservationStatus status;

    public Integer getTotalPrice () {
        return qty * reservationPrice;
    }

}