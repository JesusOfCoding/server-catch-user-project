package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.reservation.entity.Reservation;
import com.side.serverusercatchproject.modules.reservation.enums.ReservationStatus;
import com.side.serverusercatchproject.modules.reservation.repository.ReservationRepository;
import com.side.serverusercatchproject.modules.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class PaymentRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUpByReservation(null, 1, null, "예약간격", "예약취소가능날짜",
                LocalDateTime.of(2023,04,9,9,00), LocalDateTime.of(2023,04,9,9,00),
                10000, ReservationStatus.WAIT);
    }

    private Reservation setUpByReservation(User user, Integer qty, EnterpriseStoreInfo store, String reservationTerm, String reservationCancelDay
            , LocalDateTime pushTime, LocalDateTime activeTime, Integer reservationPrice, ReservationStatus status) {
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
