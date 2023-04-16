package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreMenu;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.MenuStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.event.entity.StoreEvent;
import com.side.serverusercatchproject.modules.event.enums.StoreEventStatus;
import com.side.serverusercatchproject.modules.event.repository.StoreEventRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.util.type.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class StoreEventRepositoryTest {

    @Autowired
    private StoreEventRepository storeEventRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    void selectAll() {
        var storeEvents = storeEventRepository.findAll();
        Assertions.assertNotEquals(storeEvents.size(), 0);

        StoreEvent storeEvent = storeEvents.get(0);
        Assertions.assertEquals(storeEvent.getTitle(), "이벤트제목");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalStoreEvents = this.storeEventRepository.findById(1);

        if(optionalStoreEvents.isPresent()) {
            var result = optionalStoreEvents.get();
            Assertions.assertEquals(result.getTitle(),"이벤트제목");

            var status = StoreEventStatus.ACTIVE;
            result.setStatus(status);
            StoreEvent merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getStatus(),StoreEventStatus.ACTIVE);
        } else {
            Assertions.assertNotNull(optionalStoreEvents.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        StoreEvent storeEvent = setUp("이벤트제목222", StoreEventStatus.WAIT);
        Optional<StoreEvent> findStoreEvent = this.storeEventRepository.findById(storeEvent.getId());

        if(findStoreEvent.isPresent()) {
            var result = findStoreEvent.get();
            Assertions.assertEquals(result.getTitle(), "이벤트제목222");
            entityManager.remove(storeEvent);
            Optional<StoreEvent> deleteStoreEvent = this.storeEventRepository.findById(storeEvent.getId());
            if (deleteStoreEvent.isPresent()) {
                Assertions.assertNull(deleteStoreEvent.get());
            }
        } else {
            Assertions.assertNotNull(findStoreEvent.get());
        }
    }

    private StoreEvent setUp(String title, StoreEventStatus status) {
        FileInfo fileInfo = FileInfo.builder().type(FileType.BANNER).build();
        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.USER).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();
        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();
        EnterpriseStoreMenu enterpriseStoreMenu = EnterpriseStoreMenu.builder().store(store).name("메뉴이름").price(1000).fileInfo(fileInfo).description("메뉴설명").status(MenuStatus.ACTIVE).build();

        var storeEvent = new StoreEvent();
        storeEvent.setMenu(enterpriseStoreMenu);
        storeEvent.setTitle(title);
        storeEvent.setStatus(status);
        return this.entityManager.persist(storeEvent);
    }
}
