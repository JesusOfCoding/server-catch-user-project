package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.reservation.entity.Reservation;
import com.side.serverusercatchproject.modules.reservation.repository.ReservationRepository;
import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.reservation.enums.ReservationStatus;
import com.side.serverusercatchproject.modules.user.enums.UserStatus;
import com.side.serverusercatchproject.util.type.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("예약 JPA 테스트")
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp(1, "예약간격", "예약취소가능날짜", LocalDateTime.of(2023,04,9,9,00),
                LocalDateTime.of(2023,04,9,9,00), 10000,ReservationStatus.WAIT);
    }

    @Test
    @Transactional
    @DisplayName("예약 조회")
    void selectAll() {
        List<Reservation> reservationList = reservationRepository.findAll();
        Assertions.assertNotEquals(reservationList.size(), 0);

        Reservation reservation = reservationList.get(0);
        Assertions.assertEquals(reservation.getQty(), 3);
    }

    @Test
    @Transactional
    @DisplayName("예약 조회 및 수정")
    void selectAndUpdate() {
        var optionalNotices = this.reservationRepository.findById(1);

        if(optionalNotices.isPresent()) {
            var result = optionalNotices.get();
            Assertions.assertEquals(result.getQty(),3);

            var reservationPrice = 12000;
            result.setReservationPrice(reservationPrice);
            Reservation reservation = entityManager.merge(result);

            Assertions.assertEquals(reservation.getReservationPrice(),12000);
        } else {
            Assertions.assertNotNull(optionalNotices.get());
        }
    }

    @Test
    @Transactional
    @DisplayName("예약 삽입 및 수정")
    void insertAndDelete() {
        Reservation reservation = setUp(3,  "예약간격", "예약취소가능날짜",
                LocalDateTime.of(2021,04,9,9,00), LocalDateTime.of(2021,04,9,9,00),
                20000,ReservationStatus.WAIT);
        Optional<Reservation> findNotice = this.reservationRepository.findById(reservation.getId());

        if(findNotice.isPresent()) {
            var result = findNotice.get();
            Assertions.assertEquals(result.getQty(), 3);
            entityManager.remove(reservation);
            Optional<Reservation> deleteNotice = this.reservationRepository.findById(reservation.getId());
            if (deleteNotice.isPresent()) {
                Assertions.assertNull(deleteNotice.get());
            }
        } else {
            Assertions.assertNotNull(findNotice.get());
        }
    }

    private Reservation setUp(Integer qty, String reservationTerm,String reservationCancelDay
            , LocalDateTime pushTime, LocalDateTime activeTime, Integer reservationPrice,ReservationStatus status) {
        FileInfo fileInfo = FileInfo.builder().type(FileType.MAGAZINE).build();

        User user = User.builder().username("권경렬").password("124578").role(RoleType.ACTIVE).email("kkr@nate.com").tel("010-1472-8572").status(UserStatus.ACTIVE).build();

        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.ACTIVE).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();

        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        var reservation = new Reservation();
        reservation.setUser(user);
        reservation.setQty(qty);
        reservation.setStore(store);
        reservation.setReservationTerm(reservationTerm);
        reservation.setReservationCancelDay(reservationCancelDay);
        reservation.setPushTime(pushTime);
        reservation.setActiveTime(activeTime);
        reservation.setReservationPrice(reservationPrice);
        reservation.setStatus(status);
        return this.entityManager.persist(reservation);
    }

}


