package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.reservation.entity.Reservation;
import com.side.serverusercatchproject.modules.reservation.repository.ReservationRepository;
import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.reservation.enums.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp(null, 1, null, "예약간격", "예약취소가능날짜",
                LocalDateTime.of(2023,04,9,9,00), LocalDateTime.of(2023,04,9,9,00),
                10000,ReservationStatus.WAIT);
    }

    @Test
    @Transactional
    void selectAll() {
        List<Reservation> reservationList = reservationRepository.findAll();
        Assertions.assertNotEquals(reservationList.size(), 0);

        Reservation reservation = reservationList.get(0);
        Assertions.assertEquals(reservation.getQty(), 1);
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalNotices = this.reservationRepository.findById(4);

        if(optionalNotices.isPresent()) {
            var result = optionalNotices.get();
            Assertions.assertEquals(result.getQty(),1);

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
    void insertAndDelete() {
        Reservation reservation = setUp(null, 3, null, "예약간격", "예약취소가능날짜",
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

    private Reservation setUp(User user, Integer qty, EnterpriseStoreInfo store, String reservationTerm,String reservationCancelDay
            , LocalDateTime pushTime, LocalDateTime activeTime, Integer reservationPrice,ReservationStatus status) {
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


