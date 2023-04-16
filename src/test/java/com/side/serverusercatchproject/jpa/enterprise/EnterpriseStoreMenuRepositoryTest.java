package com.side.serverusercatchproject.jpa.enterprise;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreMenu;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.MenuStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseRepository;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseStoreMenuRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
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
public class EnterpriseStoreMenuRepositoryTest {

    @Autowired
    private EnterpriseStoreMenuRepository enterpriseStoreMenuRepository;

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @Test
    @Transactional
    void selectAll() {
        var enterpriseStoreMenus = enterpriseStoreMenuRepository.findAll();
        Assertions.assertNotEquals(enterpriseStoreMenus.size(), 0);

        EnterpriseStoreMenu enterpriseStoreMenu = enterpriseStoreMenus.get(0);
        Assertions.assertEquals(enterpriseStoreMenu.getName(), "메뉴이름");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalEnterpriseStoreMenus = this.enterpriseStoreMenuRepository.findById(1);

        if(optionalEnterpriseStoreMenus.isPresent()) {
            var result = optionalEnterpriseStoreMenus.get();
            Assertions.assertEquals(result.getName(),"메뉴이름");

            var description = "메뉴 설명2";
            result.setDescription(description);
            EnterpriseStoreMenu merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getName(),"메뉴이름");
        } else {
            Assertions.assertNotNull(optionalEnterpriseStoreMenus.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        EnterpriseStoreMenu enterpriseStoreMenu = setUp("메뉴이름333", 1500, "메뉴설명333", MenuStatus.ACTIVE);
        Optional<EnterpriseStoreMenu> findEnterpriseStoreMenu = this.enterpriseStoreMenuRepository.findById(enterpriseStoreMenu.getId());

        if(findEnterpriseStoreMenu.isPresent()) {
            var result = findEnterpriseStoreMenu.get();
            Assertions.assertEquals(result.getName(), "메뉴이름333");
            entityManager.remove(enterpriseStoreMenu);
            Optional<EnterpriseStoreMenu> deleteEnterpriseStoreMenu = this.enterpriseStoreMenuRepository.findById(enterpriseStoreMenu.getId());
            if (deleteEnterpriseStoreMenu.isPresent()) {
                Assertions.assertNull(deleteEnterpriseStoreMenu.get());
            }
        } else {
            Assertions.assertNotNull(findEnterpriseStoreMenu.get());
        }
    }

    private EnterpriseStoreMenu setUp(String name, Integer price, String description, MenuStatus status) {
        FileInfo fileInfo = FileInfo.builder().type(FileType.MENU).build();
        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.ENTERPRISE).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();
        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        var enterpriseStoreMenu = new EnterpriseStoreMenu();
        enterpriseStoreMenu.setStore(store);
        enterpriseStoreMenu.setName(name);
        enterpriseStoreMenu.setPrice(price);
        enterpriseStoreMenu.setFileInfo(fileInfo);
        enterpriseStoreMenu.setDescription(description);
        enterpriseStoreMenu.setStatus(status);
        return this.entityManager.persist(enterpriseStoreMenu);
    }
}