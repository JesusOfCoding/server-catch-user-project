package com.side.serverusercatchproject.jpa.enterprise;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreCalendar;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseRepository;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseStoreCalendarRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.util.type.RoleType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EnterpriseStoreCalendarRepositoryTest {

    @Autowired
    private EnterpriseStoreCalendarRepository enterpriseStoreCalendarRepository;

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @Test
    @Transactional
    void selectAll(){
        var enterpriseStoreCalendars = enterpriseStoreCalendarRepository.findAll();
        Assertions.assertNotEquals(enterpriseStoreCalendars.size(), 0);

        EnterpriseStoreCalendar enterpriseStoreCalendar = enterpriseStoreCalendars.get(0);
        Assertions.assertEquals(enterpriseStoreCalendar.getDay(), "월요일");
    }

    @Test
    @Transactional
    void selectAndUpdate(){
        var optionalEnterpriseStoreCalendars = this.enterpriseStoreCalendarRepository.findById(1);

        if (optionalEnterpriseStoreCalendars.isPresent()){
            var result = optionalEnterpriseStoreCalendars.get();
            Assertions.assertEquals(result.getDay(), "월요일");

            var isOpen = false;
            result.setIsOpen(isOpen);

            EnterpriseStoreCalendar merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getIsOpen(), false);
        } else {
            Assertions.assertNotNull(optionalEnterpriseStoreCalendars.get());
        }
    }
    @Test
    @Transactional
    void insertAndDelete(){
        EnterpriseStoreCalendar enterpriseStoreCalendar = setUp(
                "월요일",
                LocalDateTime.of(2023,2,22,2,2,2),
                LocalDateTime.of(2023,2,22,2,2,2),
                LocalDateTime.of(2023,2,22,2,2,2),
                LocalDateTime.of(2023,2,22,2,2,2),
                true
        );
        Optional<EnterpriseStoreCalendar> findEnterpriseStoreCalendar = this.enterpriseStoreCalendarRepository.findById(enterpriseStoreCalendar.getId());

        if (findEnterpriseStoreCalendar.isPresent()){
            var result = findEnterpriseStoreCalendar.get();
            Assertions.assertEquals(result.getDay(), "월요일");
            entityManager.remove(enterpriseStoreCalendar);
            Optional<EnterpriseStoreCalendar> deleteEnterpriseStoreCalendar = this.enterpriseStoreCalendarRepository.findById(enterpriseStoreCalendar.getId());
            if (deleteEnterpriseStoreCalendar.isPresent()){
                Assertions.assertNull(deleteEnterpriseStoreCalendar.get());
            }
        } else {
            Assertions.assertNotNull(findEnterpriseStoreCalendar.get());
        }
    }

    private EnterpriseStoreCalendar setUp(
            String day,
            LocalDateTime startTime,
            LocalDateTime endTime,
            LocalDateTime startBreakTime,
            LocalDateTime endBreakTime,
            Boolean isOpen
    ){
        FileInfo fileInfo = FileInfo.builder().type(FileType.BANNER).build();
        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.ENTERPRISE).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();
        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        var enterpriseStoreCalendar = new EnterpriseStoreCalendar();

        enterpriseStoreCalendar.setStore(store);
        enterpriseStoreCalendar.setDay(day);
        enterpriseStoreCalendar.setStartTime(startTime);
        enterpriseStoreCalendar.setEndTime(endTime);
        enterpriseStoreCalendar.setStartBreakTime(startBreakTime);
        enterpriseStoreCalendar.setEndBreakTime(endBreakTime);
        enterpriseStoreCalendar.setIsOpen(isOpen);

        return this.entityManager.persist(enterpriseStoreCalendar);

    }
}